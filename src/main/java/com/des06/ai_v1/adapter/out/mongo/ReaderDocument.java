package com.des06.ai_v1.adapter.out.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("readers")
public record ReaderDocument(@Id String id, String name,
                             @Indexed(unique = true) String email) {}


