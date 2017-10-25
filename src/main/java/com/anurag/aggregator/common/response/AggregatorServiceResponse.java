package com.anurag.aggregator.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AggregatorServiceResponse extends ServiceResponse {
    private static final long serialVersionUID = 5422395445773184239L;
    private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AggregatorServiceResponse{" +
                "data=" + data +
                '}';
    }
}
