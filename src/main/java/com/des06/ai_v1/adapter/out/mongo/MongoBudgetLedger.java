package com.des06.ai_v1.adapter.out.mongo;


import java.time.Clock;
import java.time.Instant;

import com.des06.ai_v1.application.model.BudgetBalance;
import com.des06.ai_v1.application.port.out.BudgetLedger;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("hybrid")
public class MongoBudgetLedger implements BudgetLedger {
    private final MongoTemplate mongo;
    private final Clock clock;
    public MongoBudgetLedger(MongoTemplate mongo, Clock clock) {
        this.mongo = mongo;
        this.clock = clock;
    }

    @Override
    public BudgetBalance load(String id) {
        var doc = mongo.findById(id, BudgetDocument.class);
        return doc == null ? BudgetBalance.empty(id)
                : new BudgetBalance(doc.id(), doc.chargedUsd(), doc.reviewRequired());
    }
    
    @Override
    public void save(BudgetBalance balance) {
        mongo.save(new BudgetDocument(balance.id(), balance.chargedUsd(),
                balance.reviewRequired(), Instant.now(clock)));
    }
}
