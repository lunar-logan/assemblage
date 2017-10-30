package com.anurag.aggregator.controller;

import com.anurag.aggregator.common.entity.AggregatorServiceApiMapping;
import com.anurag.aggregator.common.request.AggregatorServiceRequest;
import com.anurag.aggregator.common.request.RegisterServiceRequest;
import com.anurag.aggregator.common.response.AggregatorServiceResponse;
import com.anurag.aggregator.common.response.RegisterServiceResponse;
import com.anurag.aggregator.repository.AggregatorServiceApiMappingRepository;
import com.anurag.aggregator.service.AggregatorService;
import com.anurag.aggregator.service.impl.AggregatorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * The main controller describing all the endpoints exposed by this service.
 */
@RestController
@RequestMapping(value = AggregatorWebServiceController.route)
public class AggregatorWebServiceController {
    static final Logger log = LoggerFactory.getLogger(AggregatorWebServiceController.class);

    static final String route = "/aggregator/service";

    @PostMapping(value = "/register", produces = "application/json")
    public Mono<RegisterServiceResponse> register(@RequestBody RegisterServiceRequest request) {
        return aggregatorService.register(request);
    }

    @PostMapping(value = "/aggregate", produces = "application/json")
    public Mono<AggregatorServiceResponse> aggregate(@RequestBody AggregatorServiceRequest request) {
        long st = System.nanoTime();
        Mono<AggregatorServiceResponse> response = aggregatorService.aggregate(request);
        log.info("Response emitter built in {} us", TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - st));
        return response;
    }


    @GetMapping(value = "/listMappings", produces = "application/json")
    public @ResponseBody
    Collection<AggregatorServiceApiMapping> listMappings() {
        return repository.find();
    }

    @GetMapping(value = "/test/{start}/{end}")
    public Mono<Long> test(@PathVariable("start") int start, @PathVariable("end") int end) {
        return Flux.range(start, end)
                .map(u -> (long) u)
                .map(u -> u * u)
                .reduce(0L, (u, v) -> u + v);
//                .subscribeOn(Schedulers.elastic());
    }

    @RequestMapping(path = "/sample/{delay}", method = RequestMethod.GET)
    public Flux<String> handleMessageFlux(@PathVariable("delay") long delay) {
        return this.handleMessageFlux0(delay);
    }

    public Flux<String> handleMessageFlux0(long delay) {
        return Flux.just("Hello")
                .delaySubscription(Duration.ofMillis(delay))
                .map(String::toUpperCase)
                .subscribeOn(Schedulers.elastic());
    }


    @Autowired
    public AggregatorWebServiceController(AggregatorService aggregatorService, AggregatorServiceApiMappingRepository repository) {
        this.aggregatorService = aggregatorService;
        this.repository = repository;
    }

    private final AggregatorService aggregatorService;
    private final AggregatorServiceApiMappingRepository repository;
}
