package com.daar.SeachEngineAPI.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.service.SearchIDService;

@RestController
@RequestMapping("/api/books")
public class SearchIDController {
    private SearchIDService sis;

    public SearchIDController(SearchIDService searchIDService) {
        this.sis = searchIDService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return sis.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
