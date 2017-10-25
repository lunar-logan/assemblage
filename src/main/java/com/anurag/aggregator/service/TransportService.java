package com.anurag.aggregator.service;


import com.anurag.aggregator.common.request.TransportRequest;
import com.anurag.aggregator.common.response.TransportResponse;
import com.anurag.aggregator.common.type.TransportType;

import java.util.Collection;

public interface TransportService {
    TransportResponse execute(TransportRequest request);

    Collection<TransportType> type();
}
