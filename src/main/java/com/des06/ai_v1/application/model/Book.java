package com.des06.ai_v1.application.model;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public record Book(String id, String isbn, String title,
                   String author, Set<String> topics, boolean active) {


    public Book {
        if (isbn == null || title == null || title.isBlank()
                || author == null || author.isBlank()
                || topics == null || topics.isEmpty()) {
            throw new IllegalArgumentException("Dados do livro incompletos.");
        }
        isbn = isbn.replace("-", "").replace(" ", "");
        if (!validIsbn13(isbn)) {
            throw new IllegalArgumentException("ISBN-13 invalido.");
        }
        title = title.strip();
        author = author.strip();
        topics = topics.stream().map(String::strip)
                .map(s -> s.toLowerCase(Locale.ROOT))
                .filter(s -> !s.isBlank()).collect(Collectors.toUnmodifiableSet());
        if (topics.isEmpty()) {
            throw new IllegalArgumentException("Informe ao menos um assunto.");
        }
    }

    private static boolean validIsbn13(String value) {
        if (!value.matches("(978|979)\\d{10}")) return false;
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (value.charAt(i) - '0') * (i % 2 == 0 ? 1 : 3);
        }
        return (10 - sum % 10) % 10 == value.charAt(12) - '0';
    }

}
