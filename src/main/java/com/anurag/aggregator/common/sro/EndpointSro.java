package com.anurag.aggregator.common.sro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EndpointSro implements Serializable {
    private static final long serialVersionUID = -1261873856331013063L;

    private String url;
    private Long timeout;
    private String method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "EndpointSro{" +
                "url='" + url + '\'' +
                ", timeout=" + timeout +
                ", method=" + method +
                '}';
    }
}
