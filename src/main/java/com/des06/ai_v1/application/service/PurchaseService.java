package com.des06.ai_v1.application.service;

import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.port.in.CatalogUseCase;
import com.des06.ai_v1.application.port.in.PurchaseUseCase;
import com.des06.ai_v1.application.port.in.ReaderUseCase;
import com.des06.ai_v1.application.port.out.PurchaseRepository;
import com.des06.ai_v1.domain.model.Purchase;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import static com.des06.ai_v1.application.exception.BusinessException.Kind.CONFLICT;
import static com.des06.ai_v1.application.exception.BusinessException.Kind.INVALID;

public final class PurchaseService implements PurchaseUseCase {
    private final ReaderUseCase readers;
    private final CatalogUseCase catalog;
    private final PurchaseRepository purchases;
    private final Clock clock;

    public PurchaseService(ReaderUseCase readers, CatalogUseCase catalog,
                           PurchaseRepository purchases, Clock clock) {
        this.readers = readers;
        this.catalog = catalog;
        this.purchases = purchases;
        this.clock = clock;
    }
    @Override
    public Purchase register(String readerId, String bookId) {
        readers.get(readerId);
        if (!catalog.get(bookId).active()) {
            throw new BusinessException(INVALID, "Livro inativo para nova compra.");
        }
        if (purchases.exists(readerId, bookId)) {
            throw new BusinessException(CONFLICT, "Vinculo de compra ja existe.");
        }
        return purchases.save(new Purchase(UUID.randomUUID().toString(),
                readerId, bookId, Instant.now(clock)));
    }
}

