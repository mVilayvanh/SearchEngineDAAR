package com.daar.SeachEngineAPI.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class GeometricGraph {
    private final Map<Long, Map<Long, Double>> graph = new HashMap<>();
    private GeometricGraph() {}

    public static GeometricGraph jaccardDistanceFrom(BookLexicon bookLexicon) {
        GeometricGraph res = new GeometricGraph();
        Set<Long> bookIds = bookLexicon.getAllBookIds();
        for (Long bookId1 : bookIds) { res.addVertex(bookId1);
            for (Long bookId2 : bookLexicon.getAllBookIds()) {
                if (bookId1.equals(bookId2)) continue;
                double dist = Jaccard.distance(bookLexicon.get(bookId1), bookLexicon.get(bookId2));
                res.addEdge(bookId1, bookId2, dist == 0.0 ? 0 : 1.0 / dist);
            }
        }
        return res;
    }

    public void addVertex(Long id) {
        graph.putIfAbsent(id, new HashMap<>());
    }

    public void addEdge(Long source, Long destination, Double weight) {
        graph.putIfAbsent(source, new HashMap<>());
        graph.get(source).put(destination, weight);
    }

    public Map<Long, Double> computeClosenessCentrality() {
        Map<Long, Double> closenessMap = new HashMap<>();
        for (Long source : graph.keySet()) {
            double totalDist = shortestPathSum(source);
            double closeness = totalDist == 0 ? 0 : 1.0 / totalDist;
            closenessMap.put(source, closeness); } return closenessMap;
    }

    private double shortestPathSum(Long start) {
        Map<Long, Double> dist = new HashMap<>();
        for (Long node : graph.keySet()) dist.put(node, Double.POSITIVE_INFINITY);
        dist.put(start, 0.0);
        PriorityQueue<Map.Entry<Long, Double>> pq = new PriorityQueue<>(Map.Entry.comparingByValue());
        pq.add(Map.entry(start, 0.0));
        while (!pq.isEmpty()) {
            Long u = pq.poll().getKey();
            double d = dist.get(u);
            for (Map.Entry<Long, Double> next : graph.get(u).entrySet()) {
                Long v = next.getKey();
                double w = next.getValue();
                double newDist = d + w;
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    pq.add(Map.entry(v, newDist));
                }
            }
        }
        return dist.values().stream()
                .filter(x -> !Double.isInfinite(x))
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}