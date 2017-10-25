package com.anurag.aggregator.common.response;

import com.anurag.aggregator.common.XContent;

import java.nio.charset.Charset;

public class HttpResponse extends TransportResponse {
    public HttpResponse(XContent body, int code, String reason, Charset charset) {
        this.body = body;
        this.code = code;
        this.reason = reason;
        this.charset = charset;
    }

    public HttpResponse(XContent body, int code, String reason) {
        this(body, code, reason, Charset.forName("utf-8"));
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public XContent getBody() {
        return body;
    }

    public Charset getCharset() {
        return charset;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "code=" + code +
                ", reason='" + reason + '\'' +
                ", body=" + body +
                ", charset=" + charset +
                '}';
    }

    /**
     * Defaults to <b>UTF-8</b>
     */
    private final Charset charset;
}
