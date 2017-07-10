package com.epam.cdp.m2.hw2.aggregator;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class Java8ParallelAggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        return numbers.parallelStream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        //@formatter:off
        return words.parallelStream()
                .collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .parallelStream()
                .map(stringLongEntry -> new Pair<>(stringLongEntry.getKey(), stringLongEntry.getValue()))
                .sorted(Comparator.comparing(Pair<String,Long>::getKey)
                        .thenComparing(Comparator.comparing(Pair<String,Long>::getValue)))
                .limit(limit)
                .collect(Collectors.toList());
        //@formatter:on
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        //@formatter:off
        return words.parallelStream()
                .map(String::toUpperCase)
                .collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .parallelStream()
                .filter(stringLongEntry -> stringLongEntry.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted((o1, o2) -> {
                    int comp = o1.length() - o2.length();
                    return comp == 0 ? o1.compareTo(o2) : comp;
                })
                .limit(limit)
                .collect(Collectors.toList());
        //@formatter:on
    }
}
