package com.des06.ai_v1.domain.model;

import java.time.Instant;
public record Purchase(String id, String readerId,
                       String bookId, Instant purchasedAt) {}
