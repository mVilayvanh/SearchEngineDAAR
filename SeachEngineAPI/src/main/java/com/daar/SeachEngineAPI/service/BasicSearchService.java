package com.daar.SeachEngineAPI.service;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasicSearchService {
    private final BookRepository bookRepository;
    private final RankingService rankingService;


    public BasicSearchService(BookRepository bookRepository,  RankingService rankingService) {
        this.bookRepository = bookRepository;
        this.rankingService = rankingService;
    }

    public List<Book> searchBook(String query) {
        List<Book> books = this.bookRepository.searchByFullText(query);
        return rankingService.ClosenessRanking(books);
    }
}
