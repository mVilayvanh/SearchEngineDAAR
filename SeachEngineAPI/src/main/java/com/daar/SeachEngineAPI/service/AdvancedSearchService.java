package com.daar.SeachEngineAPI.service;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.repository.BookRepository;
import com.daar.SeachEngineAPI.repository.KeywordBooks;
import com.daar.SeachEngineAPI.utils.PatternMatcher;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdvancedSearchService {
    private final BookRepository bookRepository;
    private final RankingService rankingService;

    public AdvancedSearchService(BookRepository bookRepository, RankingService rankingService) {
        this.bookRepository = bookRepository;
        this.rankingService = rankingService;
        rankingService.init();
    }

    public List<Book> searchBooks(String query) {
        PatternMatcher pm = new PatternMatcher(query);
        List<KeywordBooks> index = bookRepository.findKeywordBooks();
        List<Book> books = index.stream()
                .filter(kb -> pm.matches(kb.getKeyword()))
                .flatMap(kb -> Arrays.stream(kb.getBooks())
                        .map(bookId -> bookRepository.findById(bookId).orElse(null))
                        .filter(Objects::nonNull))
                .distinct()
                .toList();
        return rankingService.closenessRanking(books);
    }
}
