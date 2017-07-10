package com.epam.cdp.m2.hw2.aggregator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import com.epam.cdp.m2.hw2.aggregator.callable.DuplicateWordsCallable;
import com.epam.cdp.m2.hw2.aggregator.callable.FrequentWordsCallable;
import com.epam.cdp.m2.hw2.aggregator.callable.SumCallable;
import com.epam.cdp.m2.hw2.comparators.Comparators;

import javafx.util.Pair;

public class Java7ParallelAggregator implements Aggregator {

    private static final int DEFAULT_THREAD_COUNT = 10;
    private int threadCount;

    public Java7ParallelAggregator(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Thread count should be more than 0");
        }
        this.threadCount = threadCount;
    }

    public Java7ParallelAggregator() {
        threadCount = DEFAULT_THREAD_COUNT;
    }

    @Override
    public int sum(List<Integer> numbers) {
        final ParallelTaskExecutor<Integer> parallelExecutor = new ParallelTaskExecutor<>(threadCount);
        final int step = threadCount <= numbers.size() ? threadCount : numbers.size();
        for (int i = 0; i < threadCount; i++) {
            parallelExecutor.addTask(new SumCallable(numbers, step, i));
        }
        int sum = 0;
        for (final Integer partialResult : parallelExecutor.performAllTasksAndGetResults()) {
            sum += partialResult;
        }
        return sum;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        final ParallelTaskExecutor<Map<String, Long>> parallelExecutor = new ParallelTaskExecutor<>(threadCount);
        int step = threadCount <= words.size() ? threadCount : words.size();
        for (int i = 0; i < threadCount; i++) {
            parallelExecutor.addTask(new FrequentWordsCallable(words, step, i));
        }
        Map<String, Long> result = new HashMap<>();
        for (final Map<String, Long> partialResult : parallelExecutor.performAllTasksAndGetResults()) {
            combineResult(result, partialResult);
        }
        TreeSet<Map.Entry<String, Long>> sortedResult = new TreeSet(Comparators.ENTRY_WORD_FREQUENCY_DESC_ALPHABETICAL_ORDER_COMPARATOR);
        sortedResult.addAll(result.entrySet());
        return convertEntryToPair(limit, result);
    }

    private List<Pair<String, Long>> convertEntryToPair(long limit, Map<String, Long> result) {
        List<Pair<String, Long>> pairs = new ArrayList<>();
        Set<Map.Entry<String, Long>> entries = result.entrySet();
        for (Map.Entry<String, Long> entry : entries) {
            if (--limit < 0) {
                break;
            }
            pairs.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return pairs;
    }

    private void combineResult(Map<String, Long> result, Map<String, Long> partialResult) {
        for (Map.Entry<String, Long> entry : partialResult.entrySet()) {
            if (result.containsKey(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue() + result.get(entry.getKey()));
            } else {
                result.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        final ParallelTaskExecutor<Map<String, Long>> parallelExecutor = new ParallelTaskExecutor<>(threadCount);
        Collection<Callable<Map<String, Long>>> callables = new ArrayList<>();
        int step = threadCount <= words.size() ? threadCount : words.size();
        for (int i = 0; i < threadCount; i++) {
            parallelExecutor.addTask(new DuplicateWordsCallable(words, step, i));
        }
        Map<String, Long> wordCountMap = new HashMap<>();
        for (final Map<String, Long> partialResult : parallelExecutor.performAllTasksAndGetResults()) {
            combineResult(wordCountMap, partialResult);
        }
        Set<Map.Entry<String, Long>> entries = wordCountMap.entrySet();
        Set<Map.Entry<String, Long>> sortSet = removeWordsWithoutDuplicate(entries);
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : sortSet) {
            if (--limit < 0) {
                break;
            }
            result.add(entry.getKey());
        }
        return result;
    }

    private Set<Map.Entry<String, Long>> removeWordsWithoutDuplicate(Set<Map.Entry<String, Long>> entries) {
        Set<Map.Entry<String, Long>> sortSet = new TreeSet<>(Comparators.ENTRY_WORD_LENGTH_ASCENDING_ALPHABET_COMPARATOR);
        for (Map.Entry<String, Long> entry : entries) {
            if (entry.getValue() > 1) {
                sortSet.add(entry);
            }
        }
        return sortSet;
    }

}
