package com.daar.SeachEngineAPI.controllers;

import com.daar.SeachEngineAPI.entity.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/advanced-search")
public class AdvancedSearchController {
    private final AdvancedSearchController advancedSearchController;

    public AdvancedSearchController(AdvancedSearchController advancedSearchController) {
        this.advancedSearchController = advancedSearchController;
    }

    @GetMapping
    public List<Book> search(@RequestParam String query) {
        return advancedSearchController.search(query);
    }

}
