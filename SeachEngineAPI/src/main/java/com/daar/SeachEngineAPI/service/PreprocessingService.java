package com.daar.SeachEngineAPI.service;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.repository.BookRepository;
import com.daar.SeachEngineAPI.utils.BookLexicon;
import com.daar.SeachEngineAPI.utils.GeometricGraph;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PreprocessingService {
    private final BookRepository bookRepository;
    @Getter
    private final Map<String, Set<Book>> keywordsIndex = new HashMap<>();
    private final BookLexicon bookLexicon = new BookLexicon();
    private Map<Long, Double> closenessScores = new HashMap<>();

    private static final Pattern PATTERN = Pattern.compile("'(\\w+)':\\d+");

    public PreprocessingService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void init() {
        buildLexicon();
        GeometricGraph graph = GeometricGraph.jaccardDistanceFrom(bookLexicon);
        closenessScores = graph.computeClosenessCentrality();
    }

    private void buildLexicon() {
        keywordsIndex.clear();

        List<Book> bookList = bookRepository.findAllBooks();

        for (Book book : bookList) {
            Long id = book.getId();
            String tsv = book.getTsv();
            Matcher matcher = PATTERN.matcher(tsv);

            while (matcher.find()) {
                String lexeme = matcher.group(1);
                keywordsIndex.computeIfAbsent(lexeme, k -> new HashSet<>()).add(book);
                bookLexicon.put(id, lexeme);
            }
        }
    }

    public double getCloseness(Long bookId) {
        return closenessScores.getOrDefault(bookId, 0.0);
    }

}