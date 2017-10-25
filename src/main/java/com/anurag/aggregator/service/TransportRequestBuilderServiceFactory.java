package com.anurag.aggregator.service;

import com.anurag.aggregator.common.type.TransportType;

public interface TransportRequestBuilderServiceFactory {
    TransportRequestBuilderService getInstance(TransportType transportType);
}
