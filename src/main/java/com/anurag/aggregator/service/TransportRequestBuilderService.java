package com.anurag.aggregator.service;

import com.anurag.aggregator.common.TransportRequestContext;
import com.anurag.aggregator.common.type.TransportType;

import java.util.Collection;

public interface TransportRequestBuilderService<T> {
    T build(TransportRequestContext requestContext);

    Collection<TransportType> type();
}
