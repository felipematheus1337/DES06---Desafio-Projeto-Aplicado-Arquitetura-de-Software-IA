package com.des06.ai_v1.application.model;

import com.des06.ai_v1.domain.model.StudyTask;

import java.time.Instant;

public record UsageEvent(String requestId, String readerId, String bookId,
                         StudyTask task, String provider, String model,
                         Integer inputTokens, Integer outputTokens,
                         Integer totalTokens, long durationMs, Instant createdAt) {}