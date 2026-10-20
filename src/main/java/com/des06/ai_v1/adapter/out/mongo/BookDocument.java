package com.des06.ai_v1.adapter.out.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;

@Document("books")
public record BookDocument(@Id String id, @Indexed(unique = true) String isbn,
                           String title, String author,
                           Set<String> topics, boolean active){
}
