package com.daar.SeachEngineAPI.controllers;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.service.BasicSearchService;
import com.daar.SeachEngineAPI.utils.PageRank;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BasicSearchController {
    private final BasicSearchService bss;

    public BasicSearchController(BasicSearchService basicSearchService) {
        this.bss = basicSearchService;
    }

    @GetMapping
    public List<Book> search(@RequestParam String query) {
        return PageRank.computePageRank(query, bss.searchBook(query));
    }
}
