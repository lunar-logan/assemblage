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
import org.hibernate.validator.internal.metadata.aggregated.rule.ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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
        return Mono.just(request) // TODO : add caching
                .map(req -> {
                    log.info("Mapping service name, request-id {}", req.getId());
                    return req.getServiceName();
                })
                .flatMap(name ->
                        Mono.fromCallable(() -> {
                            log.info("Getting mapping for service {}, request-id: {}", name, request.getId());
                            return aggregatorServiceRepository.findByServiceName(name);
                        })
                ).flatMap(mapping ->
                        Flux.fromStream(mapping.getApis().stream())
                                .flatMap(api -> {
                                            log.info("Executing api: {}, request-id: {}", api, request.getId());
                                            return executeApiAsync(api, nullObject);// Mono.fromCallable(() -> executeApi(api, nullObject)).subscribeOn(Schedulers.elastic());
                                        }
                                )
                                .collect((Supplier<HashMap<String, Object>>) HashMap::new, HashMap::putAll)
                                .map(map -> {
                                    log.info("building Aggregator service response for map {}, request-Id: {}", map, request.getId());
                                    AggregatorServiceResponse response = new AggregatorServiceResponse();
                                    response.setData(map);
                                    response.setSuccessful(true);
                                    return response;
                                })
                ).subscribeOn(Schedulers.elastic());
    }

    /**
     * Zips the Api with its payload (if any) and returns a {@link Flux}. In case the {@link Api}'s endpoint does not consumes
     * a payload, the Api is zipped with a special sentinel object called as a {@code nullObject} declared as a static member.
     */
    private Flux<Tuple2<Api, Object>> fromApisAndRequest(AggregatorServiceApiMapping
                                                                 mapping, AggregatorServiceRequest request) {
        Map<String, Object> payloads = request.getPayloads()/* == null ? Collections.emptyMap() : request.getPayloads()*/;
        return Flux.fromStream(mapping.getApis().stream()) // mapping.getApis() must not be null, if it is, to futega
                .flatMap(api -> {
                    if (payloads != null && payloads.containsKey(api.getName())) {
                        return Mono.zip(Mono.just(api), Mono.just(payloads.get(api.getName())));
                    }
                    return Mono.zip(Mono.just(api), Mono.just(nullObject));
                });
    }

    @SuppressWarnings("unchecked")
    private Mono<HashMap<String, Object>> executeApiAsync(Api api, Object payload) {
        return executeEndpointAsync(Mono.just(api), Mono.just(payload))
                .flatMap(content -> Flux.fromStream(api.getFieldExpressionMap().entrySet().stream())
                        .flatMap(e ->
                                Mono.zip(
                                        Mono.just(e.getKey()),
                                        Mono.just(expressionExecutionServiceFactory.getInstance(ExpressionType.valueOf(e.getValue().getType()))
                                                .execute(e.getValue(), content))
                                ))
                        .collect((Supplier<HashMap<String, Object>>) HashMap::new, (map, tuple) -> map.put(tuple.getT1(), tuple.getT2())));
    }


    /**
     * Executes the Api by executing all the endpoints associated with the Api and extracting the fields from the
     * response JSON.
     *
     * @param api
     * @return
     */
    private Map<String, Object> executeApi(Api api, Object payload) {
        // TODO add transport service or defer to a rest client
        Map<String, Object> results = new HashMap<>();
        final java.lang.String response = executeEndpoint(api.getEndpoint(), payload);

        log.info("Response after executing endpoint: {}, now processing response", response);
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

    private Mono<String> executeEndpointAsync(Mono<Api> apiPublisher, Mono<Object> payloadPublisher) {
        return apiPublisher.flatMap(api ->
                webClient.method(HttpMethod.GET)
                        .uri(URI.create(api.getEndpoint().getUrl()))
                        .exchange()
                        .flatMap(clientResponse -> clientResponse.bodyToMono(String.class)));

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
    public AggregatorServiceImpl(ConverterService converterService, AggregatorServiceApiMappingRepository aggregatorServiceRepository, TransportRequestBuilderServiceFactory transportRequestBuilderFactory, /*TransportRequestBuilderService<HttpRequest> httpRequestTransportRequestBuilder,*/ TransportServiceFactory transportServiceFactory, ExpressionExecutionServiceFactory expressionExecutionServiceFactory, ExecutorServiceFactory executorServiceFactory, CacheService cacheService, WebClient webClient) {
        this.converterService = converterService;
        this.aggregatorServiceRepository = aggregatorServiceRepository;
        this.transportRequestBuilderFactory = transportRequestBuilderFactory;
        this.transportServiceFactory = transportServiceFactory;
        this.expressionExecutionServiceFactory = expressionExecutionServiceFactory;
        this.executorServiceFactory = executorServiceFactory;
        this.cacheService = cacheService;
        this.webClient = webClient;
    }

    private final ConverterService converterService;
    private final AggregatorServiceApiMappingRepository aggregatorServiceRepository;
    private final TransportRequestBuilderServiceFactory transportRequestBuilderFactory;
    private final TransportServiceFactory transportServiceFactory;
    private final ExpressionExecutionServiceFactory expressionExecutionServiceFactory;
    private final ExecutorServiceFactory executorServiceFactory;
    private final CacheService cacheService;

    private final WebClient webClient;
}
