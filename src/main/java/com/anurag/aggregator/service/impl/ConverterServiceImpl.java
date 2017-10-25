package com.anurag.aggregator.service.impl;

import com.anurag.aggregator.common.entity.AggregatorServiceApiMapping;
import com.anurag.aggregator.common.entity.Api;
import com.anurag.aggregator.common.entity.Endpoint;
import com.anurag.aggregator.common.entity.Expression;
import com.anurag.aggregator.common.request.RegisterServiceRequest;
import com.anurag.aggregator.common.response.RegisterServiceResponse;
import com.anurag.aggregator.common.sro.ApiSro;
import com.anurag.aggregator.common.sro.EndpointSro;
import com.anurag.aggregator.common.sro.ExpressionSro;
import com.anurag.aggregator.service.ConverterService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConverterServiceImpl implements ConverterService {
    @Override
    public Expression to(ExpressionSro sro) {
        Expression e = new Expression();
        e.setExpression(sro.getExpression());
        e.setType(sro.getType());
        return e;
    }

    @Override
    public Endpoint to(EndpointSro sro) {
        Endpoint e = new Endpoint();
        e.setUrl(sro.getUrl());
        e.setTimeout(sro.getTimeout());
        e.setMethod(sro.getMethod());
        return e;
    }

    @Override
    public Api to(ApiSro sro) {
        Api api = new Api();
        api.setEndpoint(to(sro.getEndpointSro()));
        api.setName(sro.getName());

        Map<String, Expression> fieldExpr = new HashMap<>();
        sro.getFieldExpressionSroMap().forEach((k, v) -> fieldExpr.put(k, to(v)));
        api.setFieldExpressionMap(fieldExpr);

        return api;
    }

    @Override
    public AggregatorServiceApiMapping to(RegisterServiceRequest request) {
        AggregatorServiceApiMapping mapping = new AggregatorServiceApiMapping();
        mapping.setServiceName(request.getServiceName());
        List<Api> apis = new ArrayList<>();
        request.getApis().forEach(apiSro -> apis.add(to(apiSro)));
        mapping.setApis(apis);
        return mapping;
    }

    @Override
    public RegisterServiceResponse to(AggregatorServiceApiMapping mapping) {
        RegisterServiceResponse response = new RegisterServiceResponse();
        response.setSuccessful(true);
        return response;
    }
}
