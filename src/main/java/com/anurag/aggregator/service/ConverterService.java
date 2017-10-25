package com.anurag.aggregator.service;

import com.anurag.aggregator.common.entity.AggregatorServiceApiMapping;
import com.anurag.aggregator.common.entity.Api;
import com.anurag.aggregator.common.entity.Endpoint;
import com.anurag.aggregator.common.entity.Expression;
import com.anurag.aggregator.common.request.RegisterServiceRequest;
import com.anurag.aggregator.common.response.RegisterServiceResponse;
import com.anurag.aggregator.common.sro.ApiSro;
import com.anurag.aggregator.common.sro.EndpointSro;
import com.anurag.aggregator.common.sro.ExpressionSro;

public interface ConverterService {
    Expression to(ExpressionSro sro);

    Endpoint to(EndpointSro sro);

    Api to(ApiSro sro);

    AggregatorServiceApiMapping to(RegisterServiceRequest request);

    RegisterServiceResponse to(AggregatorServiceApiMapping mapping);

//    AggregatorServiceResponse to(Map apiResponses);
}
