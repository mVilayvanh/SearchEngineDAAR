package com.daar.SeachEngineAPI.service;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicSearchService {
    private final BookRepository bookRepository;

    public BasicSearchService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> searchBook(String query) {
        return this.bookRepository.searchByFullText(query);
    }
}
