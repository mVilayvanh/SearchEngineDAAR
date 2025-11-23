package com.daar.SeachEngineAPI.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BookLexicon {

    private final Map<Long, Set<String>> bookLexicon = new HashMap<>();

    public void put(Long bookId, String word) {
        bookLexicon.computeIfAbsent(bookId, k -> new HashSet<>()).add(word);
    }

    public Set<String> get(Long bookId) {
        return bookLexicon.get(bookId);
    }

    public Set<Long> getAllBookIds() {
        return bookLexicon.keySet();
    }
}