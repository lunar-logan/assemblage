package com.anurag.aggregator.service;


import com.anurag.aggregator.common.type.TransportType;

public interface TransportServiceFactory {
    /**
     * Returns the transport service based on the {@link TransportType}.
     * One can get the {@code TransportType} instance from the scheme of the URI.
     * For example:<br/>
     * <code>
     *     String scheme = new URI(...);<br/>
     *     TransportType instance = TransportType.fromScheme(scheme);
     * </code>
     *
     * @param type the transport type
     * @return the {@link TransportService} instance
     * @see TransportType
     */
    TransportService getInstance(TransportType type);
}
