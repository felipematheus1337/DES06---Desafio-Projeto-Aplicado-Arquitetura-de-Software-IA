package com.des06.ai_v1.application.service;

import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.model.Book;
import com.des06.ai_v1.application.port.in.CatalogUseCase;
import com.des06.ai_v1.application.port.out.BookRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.des06.ai_v1.application.exception.BusinessException.Kind.CONFLICT;
import static com.des06.ai_v1.application.exception.BusinessException.Kind.NOT_FOUND;

public class CatalogService implements CatalogUseCase {

    private final BookRepository books;

    public CatalogService(BookRepository books) { this.books = books; }

    @Override
    public Book create(String isbn, String title, String author, Set<String> topics) {
        Book book = new Book(UUID.randomUUID().toString(),
                isbn, title, author, topics, true);
        ensureUnique(book);
        return books.save(book);
    }

    @Override
    public Book update(String id, String isbn, String title,
                       String author, Set<String> topics) {
        Book current = get(id);
        Book updated = new Book(id, isbn, title, author, topics, current.active());
        ensureUnique(updated);
        return books.save(updated);
    }
    @Override
    public Book get(String id) {
        return books.findById(id).orElseThrow(() ->
                new BusinessException(NOT_FOUND, "Livro nao encontrado."));
    }

    @Override
    public List<Book> list() { return books.findActive(100); }


    @Override
    public void deactivate(String id) {
        Book current = get(id);
        books.save(new Book(current.id(), current.isbn(), current.title(),
                current.author(), current.topics(), false));
    }
    private void ensureUnique(Book book) {
        books.findByIsbn(book.isbn()).ifPresent(existing -> {
            if (!existing.id().equals(book.id())) {
                throw new BusinessException(CONFLICT, "ISBN ja cadastrado.");
            }
        });
    }

}
