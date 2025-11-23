package com.daar.SeachEngineAPI.utils;

import java.util.HashSet;
import java.util.Set;

public class Jaccard {

    public static double distance(Set<String> a, Set<String> b) {
        if (a.isEmpty() && b.isEmpty()) return 0;

        Set<String> inter = new HashSet<>(a);
        inter.retainAll(b);

        Set<String> union = new HashSet<>(a);
        union.addAll(b);
        // We may delete 1.0 - if ranking is not working correctly
        return union.isEmpty() ? 0 : 1.0 - ((double) inter.size() / union.size());
    }
}
