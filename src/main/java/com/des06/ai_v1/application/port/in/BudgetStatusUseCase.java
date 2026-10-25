package com.des06.ai_v1.application.port.in;

import java.math.BigDecimal;

public interface BudgetStatusUseCase {

    record Status(String budgetId, BigDecimal limitUsd, BigDecimal effectiveLimitUsd,
                  BigDecimal chargedUsd, BigDecimal availableUsd,
                  boolean reviewRequired) {}
    
    Status status();
}

