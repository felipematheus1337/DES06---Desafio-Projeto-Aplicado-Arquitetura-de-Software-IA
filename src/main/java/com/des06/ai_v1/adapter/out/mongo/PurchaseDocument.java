package com.des06.ai_v1.adapter.out.mongo;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("purchases")
@CompoundIndex(name = "uk_reader_book",
        def = "{'readerId':1,'bookId':1}", unique = true)
public record PurchaseDocument(@Id String id, String readerId,
                               String bookId, Instant purchasedAt) {}
