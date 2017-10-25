package com.anurag.aggregator.service;

import com.anurag.aggregator.common.type.ExecutorType;

import java.util.concurrent.ExecutorService;

public interface ExecutorServiceFactory {
    ExecutorService getExecutor(ExecutorType executorType);
}
