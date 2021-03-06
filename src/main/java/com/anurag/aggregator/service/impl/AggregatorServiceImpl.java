package com.anurag.aggregator.service.impl;

import com.anurag.aggregator.common.TransportRequestContext;
import com.anurag.aggregator.common.entity.AggregatorServiceApiMapping;
import com.anurag.aggregator.common.entity.Api;
import com.anurag.aggregator.common.entity.Endpoint;
import com.anurag.aggregator.common.request.AggregatorServiceRequest;
import com.anurag.aggregator.common.request.RegisterServiceRequest;
import com.anurag.aggregator.common.request.TransportRequest;
import com.anurag.aggregator.common.response.AggregatorServiceResponse;
import com.anurag.aggregator.common.response.HttpResponse;
import com.anurag.aggregator.common.response.RegisterServiceResponse;
import com.anurag.aggregator.common.type.ExpressionType;
import com.anurag.aggregator.common.type.TransportType;
import com.anurag.aggregator.repository.AggregatorServiceApiMappingRepository;
import com.anurag.aggregator.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Service
public class AggregatorServiceImpl implements AggregatorService {
    private static final Logger log = LoggerFactory.getLogger(AggregatorServiceImpl.class);
    static final Object nullObject = new Object();


    @Override
    public Mono<RegisterServiceResponse> register(RegisterServiceRequest request) {
        return Mono.just(request)
                .map(converterService::to)
                .map(aggregatorServiceRepository::save)
                .flatMap(mapping -> Mono.just(converterService.to(mapping)));
    }

    @Override
    public Mono<AggregatorServiceResponse> aggregate(AggregatorServiceRequest request) {
        return Mono.just(aggregatorServiceRepository.findByServiceName(request.getServiceName())) // TODO : add caching
                .map(mapping -> {
                        /*
                        For the given mapping we need to execute every API, so we create a stream of API's
                         */

                    // Will store all the results of various asynchronous computations/ network calls
                    // {@code executeApi(...)} returns a map of {@code <String, Object>}, hence its type
                    ConcurrentHashMap<String, Object> results = new ConcurrentHashMap<>();

                    // To signal this/current thread to wait for all the asynchronous computations to finish
                    CountDownLatch signal = new CountDownLatch(mapping.getApis().size());

                    fromApisAndRequest(mapping, request)
                            .map(monolithic ->
                                    monolithic.map(tuple -> executeApi(tuple.getT1(), tuple.getT2()))
                                            .map(executionResult -> {
                                                log.info(executionResult.toString());
                                                results.putAll(executionResult);
                                                return executionResult;
                                            })
                                            .map(er -> {
                                                signal.countDown();
                                                return er;
                                            })
                                            .subscribeOn(Schedulers.elastic())
                                            .subscribe()
                            ).subscribe();


//                    ParallelFlux.from(emitters)
//                            .reduce(() -> results,
//                                    (stringObjectConcurrentHashMap, stringObjectMap) -> {
//                                        log.info(stringObjectMap.toString());
//                                        stringObjectConcurrentHashMap.putAll(stringObjectMap);
//                                        return stringObjectConcurrentHashMap;
//                                    })
//                            .map(stringObjectConcurrentHashMap -> {
//                                signal.countDown();
//                                return stringObjectConcurrentHashMap;
//                            }).subscribe();

                    // Wait for all the async computations/calls to finish, TODO: do we really have to wait here, or
                    // TODO: can we defer this to the callee?
                    try {
                        signal.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    AggregatorServiceResponse response = new AggregatorServiceResponse();
                    response.setData(results);
                    response.setSuccessful(true);
                    return response;
                });
    }

    private Flux<Mono<Tuple2<Api, Object>>> fromApisAndRequest(AggregatorServiceApiMapping mapping, AggregatorServiceRequest request) {
        Map<String, Object> payloads = request.getPayloads() == null ? Collections.emptyMap() : request.getPayloads();
        return Flux.fromStream(mapping.getApis().stream())
                .map(api -> {
                    if (payloads.containsKey(api.getName())) {
                        return Mono.zip(Mono.just(api), Mono.just(payloads.get(api.getName())));
                    }
                    return Mono.zip(Mono.just(api), Mono.just(nullObject));
                });
    }


    /**
     * Executes the Api by executing all the endpoints associated with the Api and extracting the fields from the
     * response JSON
     *
     * @param api
     * @return
     */
    private Map<String, Object> executeApi(Api api, Object payload) {
        // TODO add transport service or defer to a rest client
        Map<String, Object> results = new HashMap<>();
        final java.lang.String response = executeEndpoint(api.getEndpoint(), payload);

        log.info("Response after executing endpoint: {}", response);
        if (response != null) {
            api.getFieldExpressionMap().forEach((f, e) -> {
                try {
                    results.put(f, expressionExecutionServiceFactory.getInstance(ExpressionType.valueOf(e.getType())).execute(e, response));
                } catch (Throwable t) {
                    log.info("Exception while evaluating expression: ", t);
                }
            });
        }
        return results;
    }

    /**
     * Executes the endpoint and returns the response as a java.lang.String
     *
     * @param endpoint
     * @return the response as a String
     */
    private java.lang.String executeEndpoint(Endpoint endpoint, Object payload) {
        if (endpoint != null) {
            try {
                URI uri = new URI(endpoint.getUrl());
                log.info("Scheme found to be: {}", uri.getScheme());
                TransportType transportType = TransportType.fromScheme(uri.getScheme());
                log.info("TransportType is: {}", transportType);
                TransportService transportService = transportServiceFactory.getInstance(transportType);
                log.info("Transport service: {}", transportService);

                // TODO: use a transport request builder factory
                // TODO: Loads of REFACTORING REQUIRED!!!!
                // TODO : This is really bad code
                TransportRequestBuilderService transportRequestBuilder = transportRequestBuilderFactory.getInstance(transportType);
                if (transportRequestBuilder != null) {
                    HttpResponse response = (HttpResponse) transportService.execute((TransportRequest) transportRequestBuilder.build(new TransportRequestContext(endpoint, payload)));
//                    log.info("Response: {}", response);
                    return String.valueOf(response.getBody().content());
                }
            } catch (URISyntaxException e) {
                log.info("Exception while parsing URI: ", e);
            }
        }
        return null;
    }


    @Autowired
    public AggregatorServiceImpl(ConverterService converterService, AggregatorServiceApiMappingRepository aggregatorServiceRepository, TransportRequestBuilderServiceFactory transportRequestBuilderFactory, /*TransportRequestBuilderService<HttpRequest> httpRequestTransportRequestBuilder,*/ TransportServiceFactory transportServiceFactory, ExpressionExecutionServiceFactory expressionExecutionServiceFactory, ExecutorServiceFactory executorServiceFactory, CacheService cacheService) {
        this.converterService = converterService;
        this.aggregatorServiceRepository = aggregatorServiceRepository;
        this.transportRequestBuilderFactory = transportRequestBuilderFactory;
        this.transportServiceFactory = transportServiceFactory;
        this.expressionExecutionServiceFactory = expressionExecutionServiceFactory;
        this.executorServiceFactory = executorServiceFactory;
        this.cacheService = cacheService;
    }

    private final ConverterService converterService;
    private final AggregatorServiceApiMappingRepository aggregatorServiceRepository;
    private final TransportRequestBuilderServiceFactory transportRequestBuilderFactory;
    private final TransportServiceFactory transportServiceFactory;
    private final ExpressionExecutionServiceFactory expressionExecutionServiceFactory;
    private final ExecutorServiceFactory executorServiceFactory;
    private final CacheService cacheService;
}
