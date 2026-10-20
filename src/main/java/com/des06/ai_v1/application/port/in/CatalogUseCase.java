package com.des06.ai_v1.application.port.in;

import com.des06.ai_v1.domain.model.Book;

import java.util.List;
import java.util.Set;

public interface CatalogUseCase  {

    Book create(String isbn, String title, String author, Set<String> topics);
    Book update(String id, String isbn, String title,
                String author, Set<String> topics);
    Book get(String id);
    List<Book> list();
    void deactivate(String id);
}
