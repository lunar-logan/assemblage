package com.anurag.aggregator.common.entity;

public class Endpoint {
//    @Id
    private String id;
    private String url;
    private Long timeout;
    private String method;

    public Endpoint() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        return "Endpoint{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
