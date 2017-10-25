package com.anurag.aggregator.service.impl;

import com.anurag.aggregator.common.type.TransportType;
import com.anurag.aggregator.service.TransportRequestBuilderService;
import com.anurag.aggregator.service.TransportRequestBuilderServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransportRequestBuilderFactoryImpl implements TransportRequestBuilderServiceFactory {
    @Override
    public TransportRequestBuilderService getInstance(TransportType transportType) {
        return map.get(transportType);
    }

    @Autowired
    public TransportRequestBuilderFactoryImpl(List<TransportRequestBuilderService> transportRequestBuilders) {
        this.transportRequestBuilders = transportRequestBuilders;
    }

    @PostConstruct
    public void init() {
        Map<TransportType, TransportRequestBuilderService> builderMap = new HashMap<>();
        transportRequestBuilders.forEach(transportRequestBuilder -> {
            transportRequestBuilder.type().forEach(type -> {
                builderMap.put((TransportType) type, transportRequestBuilder);
            });
        });
        map = Collections.unmodifiableMap(builderMap);
    }

    private final List<TransportRequestBuilderService> transportRequestBuilders;
    private Map<TransportType, TransportRequestBuilderService> map;
}
