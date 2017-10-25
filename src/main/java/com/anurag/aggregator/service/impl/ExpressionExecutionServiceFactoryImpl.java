package com.anurag.aggregator.service.impl;

import com.anurag.aggregator.common.type.ExpressionType;
import com.anurag.aggregator.service.ExpressionExecutionService;
import com.anurag.aggregator.service.ExpressionExecutionServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpressionExecutionServiceFactoryImpl implements ExpressionExecutionServiceFactory {
    @Autowired
    private List<ExpressionExecutionService> expressionExecutionServices;

    private final Map<ExpressionType, ExpressionExecutionService> serviceMap = new HashMap<>();

    @PostConstruct
    public void init() {
        expressionExecutionServices.forEach(s -> serviceMap.put(s.type(), s));
    }

    @Override
    public ExpressionExecutionService getInstance(ExpressionType type) {
        return serviceMap.get(type);
    }
}
