package com.anurag.aggregator.common.type;

public enum TransportType {
    HTTP("http"),
    HTTPS("https"),
    MQTT("mqtt"),
    TCP("tcp");

    private final String scheme;

    TransportType(String scheme) {
        this.scheme = scheme;
    }

    public String getScheme() {
        return scheme;
    }

    public static TransportType fromScheme(String scheme) {
        // TODO: Can be optimized (for eg. by maintaining a scheme => Type map)
        for (TransportType transportType : TransportType.values()) {
            if (transportType.getScheme().equals(scheme))
                return transportType;
        }
        return null;
    }
}
