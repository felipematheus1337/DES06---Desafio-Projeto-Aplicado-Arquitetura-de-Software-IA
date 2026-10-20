package com.des06.ai_v1.adapter.in.web.response;

import java.time.Instant;

public record PurchaseResponse(String id, String readerId,
                               String bookId, Instant purchasedAt) {}
