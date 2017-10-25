package com.anurag.aggregator.common.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;


@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ServiceRequest implements Serializable {
    private static final long serialVersionUID = 8799881085391263503L;
    private static final AtomicLong CTR = new AtomicLong(0L);
    private final long id;

    protected ServiceRequest() {
        this.id = CTR.incrementAndGet();
    }

    public long getId() {
        return id;
    }
}
