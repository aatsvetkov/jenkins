package com.epam.cdp.m2.hw2.aggregator.callable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class DuplicateWordsCallable implements Callable<Map<String, Long>> {

    private List<String> words;
    private int step;
    private int start;

    public DuplicateWordsCallable(List<String> words, int step, int start) {
        this.words = words;
        this.step = step;
        this.start = start;
    }

    @Override
    public Map<String, Long> call() throws Exception {
        Map<String, Long> map = new HashMap<>();
        for (int i = start; i < words.size(); i += step) {
            String word = words.get(i).toUpperCase();
            if (map.containsKey(words)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1L);
            }
        }
        return map;
    }
}
