package com.anurag.aggregator.service.impl;

import com.anurag.aggregator.common.type.ExecutorType;
import com.anurag.aggregator.service.ExecutorServiceFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ExecutorFactoryImpl implements ExecutorServiceFactory {
    private volatile Map<ExecutorType, ExecutorService> executors;

    @PostConstruct
    public void init() {
        Map<ExecutorType, ExecutorService> map = new HashMap<>();
        map.put(ExecutorType.EXPRESSION_EXECUTOR, Executors.newCachedThreadPool());
        executors = Collections.unmodifiableMap(map);
    }

    @Override
    public ExecutorService getExecutor(ExecutorType executorType) {
        return executors.get(executorType);
    }
}
