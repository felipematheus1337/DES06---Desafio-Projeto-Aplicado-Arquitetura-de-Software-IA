package com.des06.ai_v1.application.model;

import java.math.BigDecimal;
import java.util.Objects;

public record BudgetBalance(String id,
                            BigDecimal chargedUsd,
                            boolean reviewRequired) {

    public BudgetBalance {
        Objects.requireNonNull(id);
        Objects.requireNonNull(chargedUsd);
        if (id.isBlank() || chargedUsd.signum() < 0) {
            throw new IllegalArgumentException("Saldo de orcamento invalido.");
        }
    }

    public static BudgetBalance empty(String id) {
        return new BudgetBalance(id, BigDecimal.ZERO, false);
    }
}

