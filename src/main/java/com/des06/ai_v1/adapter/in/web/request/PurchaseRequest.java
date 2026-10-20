package com.des06.ai_v1.adapter.in.web.request;

public record PurchaseRequest(@NotBlank String readerId, @NotBlank String bookId) {}



