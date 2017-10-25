package com.anurag.aggregator.service;

import com.anurag.aggregator.common.type.ExpressionType;

public interface ExpressionExecutionServiceFactory {
    ExpressionExecutionService getInstance(ExpressionType type);
}
