package com.daar.SeachEngineAPI.service;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.repository.BookRepository;
import com.daar.SeachEngineAPI.utils.PatternMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdvancedSearchService {
    private final PreprocessingService preprocessingService;
    private final RankingService rankingService;

    public AdvancedSearchService(PreprocessingService preprocessingService, RankingService rankingService) {
        this.preprocessingService = preprocessingService;
        this.rankingService = rankingService;
    }

    public List<Book> searchBooks(String query) {
        PatternMatcher pm = new PatternMatcher(query);
        Map<String, Set<Book>> index = preprocessingService.getKeywordsIndex();
        List<Book> books = index.entrySet().stream()
                .filter(entry -> pm.matches(entry.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .toList();
        return rankingService.ClosenessRanking(books);
    }
}
