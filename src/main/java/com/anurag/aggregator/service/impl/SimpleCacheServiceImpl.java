package com.anurag.aggregator.service.impl;

import com.anurag.aggregator.service.CacheService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimpleCacheServiceImpl implements CacheService {

    @Override
    public Object set(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object get(String key) {
        return map.get(key);
    }

    /**
     * The backing store of our simple cache
     */
    private final Map<String, Object> map = new ConcurrentHashMap<>();

}
