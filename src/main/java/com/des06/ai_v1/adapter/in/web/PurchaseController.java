package com.des06.ai_v1.adapter.in.web;

import com.des06.ai_v1.application.port.in.PurchaseUseCase;
import com.des06.ai_v1.domain.model.Purchase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.Instant;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseUseCase purchases;

    public PurchaseController(PurchaseUseCase purchases) { this.purchases = purchases; }

    public record PurchaseRequest(@NotBlank String readerId, @NotBlank String bookId) {}
    public record PurchaseResponse(String id, String readerId,
                                   String bookId, Instant purchasedAt) {}
    @PostMapping
    public ResponseEntity<PurchaseResponse> register(
            @Valid @RequestBody PurchaseRequest r) {
        Purchase p = purchases.register(r.readerId(), r.bookId());
        return ResponseEntity.created(URI.create("/api/purchases/" + p.id()))
                .body(new PurchaseResponse(p.id(), p.readerId(),
                        p.bookId(), p.purchasedAt()));
    }
}
