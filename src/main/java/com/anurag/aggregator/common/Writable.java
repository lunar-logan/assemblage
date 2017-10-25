package com.anurag.aggregator.common;

import java.io.OutputStream;

/**
 * Represents a writable entity that can serialize itself on the given output stream
 */
public interface Writable {
    /**
     * Serializes the current entity to the given output stream
     *
     * @param outputStream
     */
    void write(OutputStream outputStream) throws Exception;
}
