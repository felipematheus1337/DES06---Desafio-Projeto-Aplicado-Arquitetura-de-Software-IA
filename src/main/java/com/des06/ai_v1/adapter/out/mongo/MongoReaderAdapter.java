package com.des06.ai_v1.adapter.out.mongo;
import java.util.Optional;

import com.des06.ai_v1.application.port.out.ReaderRepository;
import com.des06.ai_v1.domain.model.Reader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MongoReaderAdapter implements ReaderRepository {
    private final MongoTemplate mongo;

    public MongoReaderAdapter(MongoTemplate mongo) { this.mongo = mongo; }

    @Override
    public Reader save(Reader r) {
        var d = mongo.save(new ReaderDocument(r.id(), r.name(), r.email()));
        return new Reader(d.id(), d.name(), d.email());
    }
    @Override
    public Optional<Reader> findById(String id) {
        return Optional.ofNullable(mongo.findById(id, ReaderDocument.class))
                .map(d -> new Reader(d.id(), d.name(), d.email()));
    }
    @Override
    public boolean existsByEmail(String email) {
        return mongo.exists(Query.query(Criteria.where("email").is(email)),
                ReaderDocument.class);
    }
}

