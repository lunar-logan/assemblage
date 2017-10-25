package com.anurag.aggregator.common.type;

public enum XContentType {
    APPLICATION_JSON("application/json"),
    APPLICATION_XML("application/xml"),
    TEXT_HTML("text/html"),
    TEXT_CSV("text/csv"),
    TEXT_PLAIN("text/plain"),
    APPLICATION_JAR("application/java-archive"),
    APPLICATION_JAVASCRIPT("application/javascript"),
    APPLICATION_OCTET_STREAM("application/octet-stream");


    XContentType(String type) {
        this.name = type;
    }

    public String getName() {
        return name;
    }

    private final String name;

}
