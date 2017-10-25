package com.anurag.aggregator.common.sro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiSro implements Serializable {
    private static final long serialVersionUID = -7007688994673169049L;

    private String name;
    private EndpointSro endpointSro;
    private Map<String, ExpressionSro> fieldExpressionSroMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EndpointSro getEndpointSro() {
        return endpointSro;
    }

    public void setEndpointSro(EndpointSro endpointSro) {
        this.endpointSro = endpointSro;
    }

    public Map<String, ExpressionSro> getFieldExpressionSroMap() {
        return fieldExpressionSroMap;
    }

    public void setFieldExpressionSroMap(Map<String, ExpressionSro> fieldExpressionSroMap) {
        this.fieldExpressionSroMap = fieldExpressionSroMap;
    }

    @Override
    public String toString() {
        return "ApiSro{" +
                "name='" + name + '\'' +
                ", endpointSro=" + endpointSro +
                ", fieldExpressionSroMap=" + fieldExpressionSroMap +
                '}';
    }
}
