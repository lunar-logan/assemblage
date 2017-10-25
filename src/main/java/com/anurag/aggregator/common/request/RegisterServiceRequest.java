package com.anurag.aggregator.common.request;

import com.anurag.aggregator.common.sro.ApiSro;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterServiceRequest extends ServiceRequest {
    private static final long serialVersionUID = 5706262770686629747L;

    private String serviceName;

    private List<ApiSro> apis;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<ApiSro> getApis() {
        return apis;
    }

    public void setApis(List<ApiSro> apis) {
        this.apis = apis;
    }

    @Override
    public String toString() {
        return "RegisterServiceRequest{" +
                "serviceName='" + serviceName + '\'' +
                ", apis=" + apis +
                '}';
    }
}
