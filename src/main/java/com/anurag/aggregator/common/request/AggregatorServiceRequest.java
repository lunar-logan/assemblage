package com.anurag.aggregator.common.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AggregatorServiceRequest extends ServiceRequest {
    private static final long serialVersionUID = -6884342591451846689L;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, Object> getPayloads() {
        return payloads;
    }

    public void setPayloads(Map<String, Object> payloads) {
        this.payloads = payloads;
    }

    @Override
    public String toString() {
        return "AggregatorServiceRequest{" +
                "serviceName='" + serviceName + '\'' +
                '}';
    }


    private String serviceName;
    /*
    Key value pair of Api.name and its payload
     */
    private Map<String, Object> payloads;
}
