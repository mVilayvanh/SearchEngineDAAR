package com.daar.SeachEngineAPI.controllers;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.service.AdvancedSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/advanced-search")
public class AdvancedSearchController {
    private final AdvancedSearchService advancedSearchService;

    public AdvancedSearchController(AdvancedSearchService advancedSearchService) {
        this.advancedSearchService = advancedSearchService;
    }

    @GetMapping
    public List<Book> search(@RequestParam("query") String query){
        return advancedSearchService.searchBooks(query);
    }
}
