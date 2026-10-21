package com.des06.ai_v1.adapter.in.web;

import com.des06.ai_v1.adapter.in.web.request.BookRequest;
import com.des06.ai_v1.adapter.in.web.response.BookResponse;
import com.des06.ai_v1.application.port.in.CatalogUseCase;
import com.des06.ai_v1.domain.model.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.net.URI;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/books")
public class BookController {

    private final CatalogUseCase catalog;

    public BookController(CatalogUseCase catalog) {
        this.catalog = catalog;
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest r) {
        Book book = catalog.create(r.isbn(), r.title(), r.author(), r.topics());
        return ResponseEntity.created(URI.create("/api/books/" + book.id()))
                .body(BookResponse.from(book));
    }

    @GetMapping
    public List<BookResponse> list() {
        return catalog.list().stream().map(BookResponse::from).toList();
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable String id) {
        return BookResponse.from(catalog.get(id));
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable String id,
                               @Valid @RequestBody BookRequest r) {
        return BookResponse.from(catalog.update(id,
                r.isbn(), r.title(), r.author(), r.topics()));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        catalog.deactivate(id);
        return ResponseEntity.noContent().build();
    }

}
