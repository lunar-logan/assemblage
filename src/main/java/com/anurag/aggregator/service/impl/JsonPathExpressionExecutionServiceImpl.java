package com.anurag.aggregator.service.impl;

import com.anurag.aggregator.common.entity.Expression;
import com.anurag.aggregator.common.type.ExpressionType;
import com.anurag.aggregator.service.ExpressionExecutionService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Service;

@Service
public class JsonPathExpressionExecutionServiceImpl implements ExpressionExecutionService<String, Object> {
    @Override
    public ExpressionType type() {
        return ExpressionType.JSON_PATH;
    }

    @Override
    public Object execute(Expression expression, String content) {
        final DocumentContext documentContext = JsonPath.parse(content);
        return documentContext.read(expression.getExpression());
    }

}
