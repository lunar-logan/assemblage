package com.anurag.aggregator.service;

/**
 * Declares a generic JVM caching service
 */
public interface CacheService {
    Object set(String key, Object value);

    Object get(String key);
}
