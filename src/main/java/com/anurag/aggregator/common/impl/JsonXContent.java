package com.anurag.aggregator.common.impl;

import com.anurag.aggregator.common.XContent;
import com.anurag.aggregator.common.type.XContentType;

import java.io.OutputStream;

public class JsonXContent implements XContent {

    public JsonXContent(String data) {
        this.data = data;
    }

    @Override
    public Object content() {
        return null;
    }

    @Override
    public XContentType type() {
        return XContentType.APPLICATION_JSON;
    }

    @Override
    public void write(OutputStream outputStream) throws Exception {
        outputStream.write(String.valueOf(content()).getBytes());
    }

    /*

    Member fields declared below
     */

    private final String data;
}
