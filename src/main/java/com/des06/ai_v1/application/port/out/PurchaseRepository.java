package com.des06.ai_v1.application.port.out;

import com.des06.ai_v1.domain.model.Purchase;

public interface PurchaseRepository {

    Purchase save(Purchase purchase);
    boolean exists(String readerId, String bookId);
}
