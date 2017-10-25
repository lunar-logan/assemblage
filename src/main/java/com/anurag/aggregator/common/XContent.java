package com.anurag.aggregator.common;

import com.anurag.aggregator.common.type.XContentType;

import java.io.OutputStream;

public interface XContent extends Writable {
    Object content();

    /**
     * Returns the content type
     */
    XContentType type();

    default void write(OutputStream outputStream) throws Exception {
        outputStream.write(String.valueOf(content()).getBytes());
    }
}
