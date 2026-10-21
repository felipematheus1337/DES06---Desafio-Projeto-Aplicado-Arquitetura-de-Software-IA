package com.des06.ai_v1.adapter.in.web.response;

import com.des06.ai_v1.domain.model.Book;

import java.util.Set;

public record BookResponse(String id, String isbn, String title,
                           String author, Set<String> topics, boolean active) {
    public static BookResponse from(Book b) {
        return new BookResponse(b.id(), b.isbn(), b.title(),
                b.author(), b.topics(), b.active());
    }
}
