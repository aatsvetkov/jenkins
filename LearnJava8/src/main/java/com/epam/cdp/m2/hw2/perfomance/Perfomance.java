package com.epam.cdp.m2.hw2.perfomance;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.epam.cdp.m2.hw2.aggregator.Java7Aggregator;
import com.epam.cdp.m2.hw2.aggregator.Java7ParallelAggregator;
import com.epam.cdp.m2.hw2.aggregator.Java8Aggregator;
import com.epam.cdp.m2.hw2.aggregator.Java8ParallelAggregator;

public class Perfomance {

    public static void main(String[] args) {
        //@formatter:off
        List<String> words = new Random()
                .ints(100, 1, 100)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        List<Integer> numbers = new Random()
                .ints(1000000, 1, 300)
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toList());
        //@formatter:on
        Java7Aggregator java7Aggregator = new Java7Aggregator();
        Java8Aggregator java8Aggregator = new Java8Aggregator();
        Java7ParallelAggregator java7ParallelAggregator = new Java7ParallelAggregator();
        Java8ParallelAggregator java8ParallelAggregator = new Java8ParallelAggregator();


        System.out.println("------------------------------------");
        test("Java7 sum", () -> java7Aggregator.sum(numbers));
        test("Java8 sum", () -> java8Aggregator.sum(numbers));
        test("Java7 sum parallel", () -> java7ParallelAggregator.sum(numbers));
        test("Java8 sum parallel", () -> java8ParallelAggregator.sum(numbers));
        System.out.println("------------------------------------");
        test("Java7 getMostFrequentWords", () -> java7Aggregator.getMostFrequentWords(words, 100));
        test("Java8 getMostFrequentWords", () -> java8Aggregator.getMostFrequentWords(words, 100));
        test("Java7 getMostFrequentWords parallel", () -> java7ParallelAggregator.getMostFrequentWords(words, 100));
        test("Java8 getMostFrequentWords parallel", () -> java8ParallelAggregator.getMostFrequentWords(words, 100));
        System.out.println("------------------------------------");
        test("Java7 getDuplicates", () -> java7Aggregator.getDuplicates(words, 100));
        test("Java8 getDuplicates", () -> java8Aggregator.getDuplicates(words, 100));
        test("Java7 getDuplicates parallel", () -> java7ParallelAggregator.getDuplicates(words, 100));
        test("Java8 getDuplicates parallel", () -> java8ParallelAggregator.getDuplicates(words, 100));
        System.out.println("------------------------------------");
    }

    private static void test(String testName, Runnable runnable) {
        long startTime = System.nanoTime();
        runnable.run();
        long stopTime = System.nanoTime();
        System.out.println(testName + " " + (stopTime - startTime));
    }

}
