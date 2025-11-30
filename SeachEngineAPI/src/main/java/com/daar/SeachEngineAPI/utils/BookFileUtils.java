package com.daar.SeachEngineAPI.utils;

import java.util.stream.Stream;

public class BookFileUtils {

    public static TitleAuthor extractTitleAndAuthor(String content) {
        if (content == null || content.isBlank()) return new TitleAuthor("Unknown","Unknown");
        String title = Stream.of(content.split("\n"))
                .filter(line -> line.toLowerCase().startsWith("title:"))
                .map(line -> line.replaceFirst("(?i)title:", "").trim())
                .findFirst().orElse("Unknown");
        String author = Stream.of(content.split("\n"))
                .filter(line -> line.toLowerCase().startsWith("author:"))
                .map(line -> line.replaceFirst("(?i)author:", "").trim())
                .findFirst().orElse("Unknown");
        return new TitleAuthor(title, author);
    }
}
