package com.anurag.aggregator.common.entity;

import java.util.List;

public class AggregatorServiceApiMapping {
//    @Id
    private String id;

//    @Indexed
    private String serviceName;
    private List<Api> apis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<Api> getApis() {
        return apis;
    }

    public void setApis(List<Api> apis) {
        this.apis = apis;
    }

    @Override
    public String toString() {
        return "AggregatorServiceApiMapping{" +
                "id='" + id + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", apis=" + apis +
                '}';
    }
}
