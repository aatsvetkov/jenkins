package com.epam.cdp.m2.hw2.comparators;

import java.util.Comparator;
import java.util.Map;

public class Comparators {

    public static final Comparator<Map.Entry<String, Long>> ENTRY_WORD_FREQUENCY_DESC_ALPHABETICAL_ORDER_COMPARATOR = new Comparator<Map.Entry<String, Long>>() {
        @Override
        public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
            int comp = o2.getValue().compareTo(o1.getValue());
            return comp == 0 ? o1.getKey().compareTo(o2.getKey()) : comp;
        }
    };
    public static final Comparator<Map.Entry<String, Long>> ENTRY_WORD_LENGTH_ASCENDING_ALPHABET_COMPARATOR = new Comparator<Map.Entry<String, Long>>() {
        @Override
        public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
            int comp = Integer.compare(o1.getKey().length(), o2.getKey().length());
            return comp == 0 ? o1.getKey().compareTo(o2.getKey()) : comp;
        }
    };

}
