package com.daar.SeachEngineAPI.config;

import com.daar.SeachEngineAPI.entity.Book;
import com.daar.SeachEngineAPI.repository.BookRepository;
import com.daar.SeachEngineAPI.utils.BookFileUtils;
import com.daar.SeachEngineAPI.utils.TitleAuthor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.logging.Logger;

@Component
public class DataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final ResourceLoader resourceLoader;
    private final Logger logger =  Logger.getLogger(DataLoader.class.getName());

    public DataLoader(BookRepository bookRepository, ResourceLoader resourceLoader) {
        this.bookRepository = bookRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(String... args) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:books");
        Path folder = resource.getFile().toPath();
        try (Stream<Path> stream = Files.walk(folder)) {
            List<Path> paths = stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .toList();
            List<Book> booksToInsert = new ArrayList<>();
            for (Path path : paths) {
                String content = Files.readString(path, StandardCharsets.UTF_8);
                TitleAuthor ta = BookFileUtils.extractTitleAndAuthor(content);
                if(ta.isInvalid()) {
                    logger.info("file:" + path.toString() + "\nInvalid book; skipping insertion");
                    continue;
                }
                if (!bookRepository.existsByTitleAndAuthor(ta.title(), ta.author())) {
                    Book book = new Book();
                    book.setTitle(ta.title());
                    book.setAuthor(ta.author());
                    book.setContent(content);
                    booksToInsert.add(book);
                } else {
                    logger.info("file:" + path.toString() + "\nAlready inserted book; skipping insertion");
                }
            }
            bookRepository.saveAll(booksToInsert);
            logger.info("insertion done of " +  booksToInsert.size() + " books");
        }
    }
}
