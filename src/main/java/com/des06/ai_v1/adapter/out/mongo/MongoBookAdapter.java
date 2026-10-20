package com.des06.ai_v1.adapter.out.mongo;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.des06.ai_v1.application.mongo.BookDocument;
import com.des06.ai_v1.application.port.out.BookRepository;
import com.des06.ai_v1.domain.model.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public class MongoBookAdapter  implements BookRepository {

    private final MongoTemplate mongo;

    public MongoBookAdapter(MongoTemplate mongo) { this.mongo = mongo; }

    @Override
    public Book save(Book b) {
        var document = new BookDocument(b.id(), b.isbn(), b.title(),
                b.author(), b.topics(), b.active());
        return toDomain(mongo.save(document));
    }
    @Override
    public Optional<Book> findById(String id) {
        return Optional.ofNullable(mongo.findById(id, BookDocument.class))
                .map(this::toDomain);
    }
    @Override
    public Optional<Book> findByIsbn(String isbn) {
        var query = Query.query(Criteria.where("isbn").is(isbn));
        return Optional.ofNullable(mongo.findOne(query, BookDocument.class))
                .map(this::toDomain);
    }

    @Override
    public List<Book> findActive(int limit) {
        return find(Criteria.where("active").is(true), limit);
    }
    @Override
    public List<Book> findActiveByTopic(String topic, int limit) {
        String normalized = topic.strip().toLowerCase(Locale.ROOT);
        return find(Criteria.where("active").is(true)
                .and("topics").is(normalized), limit);
    }
    private List<Book> find(Criteria criteria, int limit) {
        var query = Query.query(criteria).with(Sort.by("title"))
                .limit(Math.min(Math.max(limit, 1), 100));
        return mongo.find(query, BookDocument.class).stream()
                .map(this::toDomain).toList();
    }
    private Book toDomain(BookDocument d) {
        return new Book(d.id(), d.isbn(), d.title(),
                d.author(), d.topics(), d.active());
    }

}
