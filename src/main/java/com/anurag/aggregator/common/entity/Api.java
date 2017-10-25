package com.anurag.aggregator.common.entity;

import java.util.Map;

public class Api {
//    @Id
    private String id;
    private String name;
    private Endpoint endpoint;
    private Map<String, Expression> fieldExpressionMap;

    // TODO: add payload field in case of POST/PUT request

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Expression> getFieldExpressionMap() {
        return fieldExpressionMap;
    }

    public void setFieldExpressionMap(Map<String, Expression> fieldExpressionMap) {
        this.fieldExpressionMap = fieldExpressionMap;
    }

    @Override
    public String toString() {
        return "Api{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", endpoint=" + endpoint +
                ", fieldExpressionMap=" + fieldExpressionMap +
                '}';
    }
}
