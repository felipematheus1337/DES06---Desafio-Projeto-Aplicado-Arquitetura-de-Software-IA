package com.des06.ai_v1.application.port.in;


import com.des06.ai_v1.domain.model.Purchase;

public interface PurchaseUseCase {

    Purchase register(String readerId, String bookId);
}
