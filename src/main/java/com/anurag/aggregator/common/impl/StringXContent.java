package com.anurag.aggregator.common.impl;

import com.anurag.aggregator.common.XContent;
import com.anurag.aggregator.common.type.XContentType;

/**
 * Represents a plain string content
 */
public class StringXContent implements XContent {

    @Override
    public Object content() {
        return content;
    }

    @Override
    public XContentType type() {
        return XContentType.TEXT_PLAIN;
    }

    public StringXContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "StringXContent{" +
                "content='" + content + '\'' +
                '}';
    }

    private final String content;
}
