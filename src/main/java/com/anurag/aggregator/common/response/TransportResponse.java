package com.anurag.aggregator.common.response;

import com.anurag.aggregator.common.XContent;

public class TransportResponse {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public XContent getBody() {
        return body;
    }

    public void setBody(XContent body) {
        this.body = body;
    }

    protected int code;
    protected String reason;
    protected XContent body;
}
