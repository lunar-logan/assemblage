package com.anurag.aggregator.service;

import com.anurag.aggregator.common.request.AggregatorServiceRequest;
import com.anurag.aggregator.common.request.RegisterServiceRequest;
import com.anurag.aggregator.common.response.AggregatorServiceResponse;
import com.anurag.aggregator.common.response.RegisterServiceResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;

/**
 * Declares the main aggregator service
 * <h2>Responsibilities</h2>
 * <ul>
 * <li>Performs the aggregation operations </li>
 * </ul>
 */
public interface AggregatorService {

    Mono<RegisterServiceResponse> register(RegisterServiceRequest request);

    Mono<AggregatorServiceResponse> aggregate(AggregatorServiceRequest request);

}
