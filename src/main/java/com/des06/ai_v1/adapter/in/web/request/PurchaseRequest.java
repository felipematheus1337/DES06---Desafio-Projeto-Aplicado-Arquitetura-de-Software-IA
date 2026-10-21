package com.des06.ai_v1.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;

public record PurchaseRequest(@NotBlank String readerId, @NotBlank String bookId) {}



