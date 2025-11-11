package com.daar.SeachEngineAPI.controllers;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.service.BasicSearchService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BasicSearchController {
    private final BasicSearchService bss;

    public  BasicSearchController(BasicSearchService basicSearchService) {
        this.bss = basicSearchService;
    }

    public List<Book> search(@RequestParam String query) {
        return this.bss.searchBook(query);
    }
}
