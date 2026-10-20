package com.des06.ai_v1.application.port.out;

import com.des06.ai_v1.application.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);
    Optional<Book> findById(String id);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findActive(int limit);
    List<Book> findActiveByTopic(String topic, int limit);
}
