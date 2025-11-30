package com.daar.SeachEngineAPI.service;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.repository.BookRepository;
import com.daar.SeachEngineAPI.repository.KeywordBooks;
import com.daar.SeachEngineAPI.utils.BookLexicon;
import com.daar.SeachEngineAPI.utils.GeometricGraph;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class RankingService {
    private final BookRepository bookRepository;
    private final BookLexicon bookLexicon = new BookLexicon();
    @Getter
    private Map<Long, Double> closenessScores = new HashMap<>();
    private boolean isInitialized = false;
    private final Logger logger = Logger.getLogger(RankingService.class.getName());

    public RankingService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void init() {
        if (isInitialized) return;
        buildLexicon();
        GeometricGraph graph = GeometricGraph.jaccardDistanceFrom(bookLexicon);
        closenessScores = graph.computeClosenessCentrality();
        logger.info("score established");
        isInitialized = true;
    }

    private void buildLexicon() {
        List<KeywordBooks> kbs = bookRepository.findKeywordBooks();
        for (KeywordBooks kb : kbs) {
            String word = kb.getKeyword();
            for (Long id : kb.getBooks()) {
                bookLexicon.put(id, word);
            }
        }
    }

    public List<Book> closenessRanking(List<Book> books) {
        return books.stream()
                .sorted((a, b) -> Double.compare(
                        closenessScores.getOrDefault(b.getId(), 0.0),
                        closenessScores.getOrDefault(a.getId(), 0.0)
                ))
                .toList();
    }
}