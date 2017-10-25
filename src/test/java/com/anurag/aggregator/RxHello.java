package com.anurag.aggregator;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class RxHello {
    static Flowable<Integer> power(int u, int v) {
        return Flowable.range(u, v)
                .map(x -> x * x)
                .delay(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation());
    }

    public static void main(String[] args) throws InterruptedException {
        power(1, 10).forEach(x -> System.out.println(x));
        Thread.sleep(10000);
    }
}
