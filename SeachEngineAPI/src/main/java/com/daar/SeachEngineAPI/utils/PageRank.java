package com.daar.SeachEngineAPI.utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

import com.daar.SeachEngineAPI.entity.Book;

public class PageRank {

    private static double jaccardIndex(Set<String> setA, Set<String> setB) {
        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }


    public static List<Book> computePageRank(String query,List<Book> books) {
        Set<String> queryWords = Arrays.stream(query.toLowerCase().split("\\s+"))
                                       .collect(Collectors.toSet());

        Map<Book, Double> scores = new HashMap<>();
        for(Book book : books) {
            Set<String> keywords = book.getKeywords();
            double score = jaccardIndex(keywords, queryWords) * 0.9;
            scores.put(book, score);
        }

        List<Map.Entry<Book, Double>> sortedEntries = scores.entrySet().stream()
                .sorted(Map.Entry.<Book, Double>comparingByValue().reversed())
                .toList();

        return sortedEntries.stream()
                            .map(Map.Entry::getKey)
                            .toList();
    }
}
