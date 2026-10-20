package com.des06.ai_v1.adapter.out.mongo;

import com.des06.ai_v1.application.port.out.PurchaseRepository;
import com.des06.ai_v1.domain.model.Purchase;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MongoPurchaseAdapter implements PurchaseRepository {
    private final MongoTemplate mongo;

    public MongoPurchaseAdapter(MongoTemplate mongo) { this.mongo = mongo; }

    @Override
    public Purchase save(Purchase p) {
        var d = mongo.save(new PurchaseDocument(
                p.id(), p.readerId(), p.bookId(), p.purchasedAt()));
        return new Purchase(d.id(), d.readerId(), d.bookId(), d.purchasedAt());
    }
    @Override
    public boolean exists(String readerId, String bookId) {
        var query = Query.query(Criteria.where("readerId").is(readerId)
                .and("bookId").is(bookId));
        return mongo.exists(query, PurchaseDocument.class);
    }
}
