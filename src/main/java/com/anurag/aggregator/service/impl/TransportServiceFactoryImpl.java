package com.anurag.aggregator.service.impl;


import com.anurag.aggregator.common.type.TransportType;
import com.anurag.aggregator.service.TransportService;
import com.anurag.aggregator.service.TransportServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * {@inheritDoc}
 */
@Service
public class TransportServiceFactoryImpl implements TransportServiceFactory {

    @Override
    public TransportService getInstance(TransportType type) {
        return serviceMap.get(type);
    }

    @PostConstruct
    public void init() {
        for (TransportService transportService : transportServiceList) {
            Collection<TransportType> serviceTypes = transportService.type();
            for (TransportType serviceType : serviceTypes) {
                serviceMap.put(serviceType, transportService);
            }
        }
        serviceMap = Collections.unmodifiableMap(serviceMap);
    }


    @Autowired
    private List<TransportService> transportServiceList;

    /**
     * Map to store all the instances of {@link TransportService} indexed by their
     * respective {@link TransportType}
     */
    private Map<TransportType, TransportService> serviceMap = new HashMap<>();

}
