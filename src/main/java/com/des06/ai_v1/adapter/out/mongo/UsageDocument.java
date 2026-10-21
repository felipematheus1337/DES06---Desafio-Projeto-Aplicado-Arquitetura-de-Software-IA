package com.des06.ai_v1.adapter.out.mongo;

import com.des06.ai_v1.domain.model.StudyTask;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("ai_usage")
public record UsageDocument(@Id String requestId, String readerId, String bookId,
                            StudyTask task, String provider, String model,
                            Integer inputTokens, Integer outputTokens,
                            Integer totalTokens, long durationMs, Instant createdAt) {}
