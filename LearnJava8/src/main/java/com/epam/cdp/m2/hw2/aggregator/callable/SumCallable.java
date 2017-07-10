package com.epam.cdp.m2.hw2.aggregator.callable;

import java.util.List;
import java.util.concurrent.Callable;

public class SumCallable implements Callable<Integer> {

    private List<Integer> numbers;
    private int step;
    private int start;

    public SumCallable(List<Integer> numbers, int step, int start) {
        this.numbers = numbers;
        this.step = step;
        this.start = start;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = start; i < numbers.size(); i += step) {
            sum += numbers.get(i);
        }
        return sum;
    }
}