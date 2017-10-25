package com.anurag.aggregator.service.impl;

import com.anurag.aggregator.common.impl.StringXContent;
import com.anurag.aggregator.common.request.HttpRequest;
import com.anurag.aggregator.common.request.TransportRequest;
import com.anurag.aggregator.common.response.HttpResponse;
import com.anurag.aggregator.common.response.TransportResponse;
import com.anurag.aggregator.common.type.TransportType;
import com.anurag.aggregator.service.TransportService;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import static com.anurag.aggregator.common.request.HttpRequest.Method.*;

@Service
public class HttpTransportServiceImpl implements TransportService {
    private static final Logger log = LoggerFactory.getLogger(HttpTransportServiceImpl.class);

    private static URI buildUri(HttpRequest request) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(request.getUrl());
        request.getQueryParameters().forEach(uriBuilder::addParameter);
        return uriBuilder.build();
    }

    private static void addHeaders(HttpUriRequest uriRequest, HttpRequest httpRequest) {
        httpRequest.getHeaders().forEach(uriRequest::addHeader);
    }

    private static HttpUriRequest buildGet(HttpRequest request) throws URISyntaxException {
        HttpGet get = new HttpGet(buildUri(request));
        addHeaders(get, request);
        return get;
    }

    private static HttpUriRequest buildPost(HttpRequest httpRequest) throws URISyntaxException {
        HttpPost post = new HttpPost(buildUri(httpRequest));
//        post.setEntity(new);
        addHeaders(post, httpRequest);
        return post;

    }

    private static HttpResponse buildResponse(org.apache.http.HttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        return new HttpResponse(
                new StringXContent(EntityUtils.toString(response.getEntity())),
                statusLine.getStatusCode(),
                statusLine.getReasonPhrase()
        );
    }

    @Override
    public TransportResponse execute(TransportRequest request) {
        if (request instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) request;
            try {
                HttpUriRequest uriRequest = null;
                switch (findByName(httpRequest.getMethod())) {
                    case GET:
                        uriRequest = buildGet(httpRequest);
                        break;
                    case POST:
                        uriRequest = buildPost(httpRequest);
                        break;
                }
                org.apache.http.HttpResponse uriResponse = httpClient.execute(uriRequest);
                return buildResponse(uriResponse);
            } catch (Throwable t) {
                log.info("Exception while executing request: ", t);
            }
        }
        return null;
    }


    @Override
    public Collection<TransportType> type() {
        return Arrays.asList(TransportType.HTTP, TransportType.HTTPS);
    }


    @Autowired
    public HttpTransportServiceImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    /*

    Member fields declaration below
     */

    private final HttpClient httpClient;
}
