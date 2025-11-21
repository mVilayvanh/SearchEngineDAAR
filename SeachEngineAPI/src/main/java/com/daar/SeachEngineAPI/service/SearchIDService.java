package com.daar.SeachEngineAPI.service;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.repository.BookRepository;

import java.util.Optional;

import org.springframework.stereotype.Service;


@Service
public class SearchIDService {
    private final BookRepository bookRepository;

    public SearchIDService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
}
