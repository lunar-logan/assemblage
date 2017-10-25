package com.anurag.aggregator.common.request;

import com.anurag.aggregator.common.XContent;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends TransportRequest {
    public static HttpRequest get(String url) {
        return new HttpRequest(Method.GET, url);
    }
    public static HttpRequest post(String url) {
        return new HttpRequest(Method.POST, url);
    }

    public enum Method {
        GET, HEAD, POST, PUT, DELETE, OPTIONS;

        public static Method findByName(String name) {
            for (Method m : values()) {
                if (m.name().equalsIgnoreCase(name.trim())) {
                    return m;
                }
            }
            return null;
        }
    }

    public HttpRequest(Method method, String url) {
        this.url = url;
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method.name();
    }


    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "body=" + body +
                ", method=" + method +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                '}';
    }

    private final Method method;
    private final Map<String, String> queryParameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();
}
