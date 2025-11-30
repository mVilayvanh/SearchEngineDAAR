package com.daar.SeachEngineAPI.utils;

import java.util.Objects;

public record TitleAuthor(String title, String author) {

    public TitleAuthor {
        Objects.requireNonNull(title);
        Objects.requireNonNull(author);
    }

    public boolean isInvalid() {
        return title.equals("Unknown") && author.equals("Unknown");
    }
}
