package com.epam.cdp.m2.hw2.aggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.epam.cdp.m2.hw2.comparators.Comparators;

import javafx.util.Pair;

public class Java7Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        int result = 0;
        for (Integer number : numbers) {
            result += number;
        }
        return result;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        Map<String, Long> wordCountMap = new HashMap<>();
        populateMap(words, wordCountMap);
        TreeSet<Map.Entry<String, Long>> sortSet = new TreeSet<>(Comparators.ENTRY_WORD_FREQUENCY_DESC_ALPHABETICAL_ORDER_COMPARATOR);
        sortSet.addAll(wordCountMap.entrySet());
        List<Pair<String, Long>> resultList = getPairs(limit, sortSet);
        return resultList;
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        Map<String, Long> wordCountMap = new HashMap<>();
        populateMapUpperCase(words, wordCountMap);
        Set<Map.Entry<String, Long>> entries = wordCountMap.entrySet();
        Set<Map.Entry<String, Long>> sortSet = new TreeSet<>(Comparators.ENTRY_WORD_LENGTH_ASCENDING_ALPHABET_COMPARATOR);
        for (Map.Entry<String, Long> entry : entries) {
            if (entry.getValue() > 1) {
                sortSet.add(entry);
            }
        }
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : sortSet) {
            if (--limit < 0) {
                break;
            }
            result.add(entry.getKey());
        }
        return result;
    }

    private List<Pair<String, Long>> getPairs(long limit, TreeSet<Map.Entry<String, Long>> sortSet) {
        List<Pair<String, Long>> resultList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : sortSet) {
            if (--limit < 0) {
                break;
            }
            resultList.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return resultList;
    }

    private void populateMap(List<String> words, Map<String, Long> map) {
        for (String word : words) {
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1L);
            }
        }
    }

    private void populateMapUpperCase(List<String> words, Map<String, Long> map) {
        for (String word : words) {
            word = word.toUpperCase();
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1L);
            }
        }
    }

}
