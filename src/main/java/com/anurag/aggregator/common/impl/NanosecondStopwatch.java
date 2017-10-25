package com.anurag.aggregator.common.impl;

import com.anurag.aggregator.common.Stopwatch;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class NanosecondStopwatch implements Stopwatch {
    private long start;
    private long stop;
    private Deque<LapsNode> laps = new LinkedList<>();
    private int ctr = 0;

    private static class LapsNode {
        final long ts;
        final String opName;

        public LapsNode(long ts, String opName) {
            this.ts = ts;
            this.opName = opName;
        }
    }

    @Override
    public long start() {
        return start = System.nanoTime();
    }

    @Override
    public void lap() {
        lap(String.format("Operation %d", ++ctr));
    }

    @Override
    public void lap(String name) {
        Objects.requireNonNull(name);
        long last = laps.isEmpty() ? start : laps.peekFirst().ts;
        laps.push(new LapsNode(System.nanoTime() - last, name));
    }

    @Override
    public long stop() {
        lap();
        this.stop = System.nanoTime();
        return this.stop;
    }

    @Override
    public long total() {
        return stop - start;
    }

    @Override
    public void reset() {
        laps.clear();
        stop = start = 0;
    }

    @Override
    public String toString(TimeUnit tu) {
        StringBuilder sb = new StringBuilder();
        laps.forEach(u ->
                sb.append(u.opName.trim())
                        .append(" completed in: ")
                        .append(tu.convert(u.ts, TimeUnit.NANOSECONDS))
                        .append("\n")
        );
        return sb.toString();
    }
}
