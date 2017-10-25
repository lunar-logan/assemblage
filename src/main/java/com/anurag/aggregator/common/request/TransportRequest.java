package com.anurag.aggregator.common.request;

import com.anurag.aggregator.common.XContent;

public class TransportRequest {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public XContent getBody() {
        return body;
    }

    public void setBody(XContent body) {
        this.body = body;
    }

    protected String url;
    protected XContent body;
}
