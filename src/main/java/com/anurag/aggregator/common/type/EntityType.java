package com.anurag.aggregator.common.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EntityType {
    ENDPOINT("url"),
    EXPRESSION("expression"),
    API,
    AGGREGATOR_SERVICE_API_MAPPING;


    private final List<String> arguments;

    EntityType(String... args) {
        arguments = new ArrayList<>();
        arguments.addAll(Arrays.asList(args));
    }

    public List<String> getArguments() {
        return arguments;
    }
}
