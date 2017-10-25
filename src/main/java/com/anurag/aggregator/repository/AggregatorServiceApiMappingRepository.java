package com.anurag.aggregator.repository;

import com.anurag.aggregator.common.entity.AggregatorServiceApiMapping;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

public interface AggregatorServiceApiMappingRepository { // extends ReactiveCrudRepository<AggregatorServiceApiMapping, String> {

    AggregatorServiceApiMapping save(AggregatorServiceApiMapping mapping);

    /**
     * Returns the configuration of the given service from the database
     *
     * @param serviceName name of the service of which the config needs to be fetched
     * @return {@link Mono<AggregatorServiceApiMapping>}
     */
    AggregatorServiceApiMapping findByServiceName(String serviceName);


    Collection<AggregatorServiceApiMapping> find();
}
