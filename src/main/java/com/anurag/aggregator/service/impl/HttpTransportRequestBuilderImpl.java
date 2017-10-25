package com.anurag.aggregator.service.impl;

import com.anurag.aggregator.common.TransportRequestContext;
import com.anurag.aggregator.common.entity.Endpoint;
import com.anurag.aggregator.common.request.HttpRequest;
import com.anurag.aggregator.common.type.TransportType;
import com.anurag.aggregator.service.TransportRequestBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

@Service
public class HttpTransportRequestBuilderImpl implements TransportRequestBuilderService<HttpRequest> {
    private static final Logger log = LoggerFactory.getLogger(HttpTransportRequestBuilderImpl.class);

    @Override
    public HttpRequest build(TransportRequestContext requestContext) {
        HttpRequest request = null;
        Endpoint endpoint = requestContext.getEndpoint();
        try {
            URI uri = new URI(endpoint.getUrl());
            switch (uri.getScheme().trim().toLowerCase()) {
                case "http":
                case "https":
                    request = new HttpRequest(HttpRequest.Method.findByName(endpoint.getMethod()), endpoint.getUrl());
            }
        } catch (URISyntaxException e) {
            log.info("Exception while building request: ", e);
        }
        return request;
    }

    @Override
    public Collection<TransportType> type() {
        return Arrays.asList(TransportType.HTTPS, TransportType.HTTP);
    }
}
