package com.anurag.aggregator.common;

import com.anurag.aggregator.common.entity.Endpoint;

public class TransportRequestContext {

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public TransportRequestContext(Endpoint endpoint, Object payload) {
        this.endpoint = endpoint;
        this.payload = payload;
    }

    private final Endpoint endpoint;
    private final Object payload;
}
