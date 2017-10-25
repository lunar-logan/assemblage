package com.anurag.aggregator.service;

import com.anurag.aggregator.common.entity.Expression;
import com.anurag.aggregator.common.type.ExpressionType;

public interface ExpressionExecutionService<T, R> {
    ExpressionType type();

    R execute(Expression expression, T content);
}
