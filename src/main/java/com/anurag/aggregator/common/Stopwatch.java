package com.anurag.aggregator.common;

import java.util.concurrent.TimeUnit;

public interface Stopwatch {
    long start();

    void lap();

    void lap(String name);

    long stop();

    long total();

    void reset();

    String toString(TimeUnit tu);
}
