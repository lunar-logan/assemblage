package com.anurag.aggregator.repository.impl;

import com.anurag.aggregator.common.entity.AggregatorServiceApiMapping;
import com.anurag.aggregator.repository.AggregatorServiceApiMappingRepository;
import com.anurag.aggregator.service.AggregatorService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AggregatorServiceApiMappingRepositoryImpl implements AggregatorServiceApiMappingRepository {
    static final AtomicInteger ctr = new AtomicInteger(0);

    @Override
    public AggregatorServiceApiMapping save(AggregatorServiceApiMapping mapping) {
        mapping.setId(String.valueOf(ctr.incrementAndGet()));
        mappings.put(mapping.getServiceName(), mapping);
        return mapping;
    }

    @Override
    public AggregatorServiceApiMapping findByServiceName(String serviceName) {
        return mappings.get(serviceName);
    }

    @Override
    public Collection<AggregatorServiceApiMapping> find() {
        return mappings.values();
    }

    private final Map<String, AggregatorServiceApiMapping> mappings = new ConcurrentHashMap<>();
}
