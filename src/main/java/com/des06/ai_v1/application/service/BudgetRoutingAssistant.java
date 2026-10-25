package com.des06.ai_v1.application.service;

import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.model.BudgetBalance;
import com.des06.ai_v1.application.port.in.BudgetStatusUseCase;
import com.des06.ai_v1.application.port.out.BudgetLedger;
import com.des06.ai_v1.application.port.out.StudyAssistant;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static com.des06.ai_v1.application.exception.BusinessException.Kind.UNAVAILABLE;


public final class BudgetRoutingAssistant implements StudyAssistant, BudgetStatusUseCase {

    private final StudyAssistant cloud;
    private final StudyAssistant local;
    private final BudgetLedger ledger;
    private final TextCostPolicy prices;
    private final String budgetId;
    private final BigDecimal limit;
    private final BigDecimal effectiveLimit;
    private final int maxPromptBytes;
    private final Consumer<String> events;

    public BudgetRoutingAssistant(StudyAssistant cloud, StudyAssistant local,
                                  BudgetLedger ledger, TextCostPolicy prices, String budgetId,
                                  BigDecimal limit, BigDecimal useFraction, int maxPromptBytes,
                                  Consumer<String> events) {
        if (limit.signum() < 0 || useFraction.signum() <= 0
                || useFraction.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Limite ou fracao invalidos.");
        }
        this.cloud = cloud;
        this.local = local;
        this.ledger = ledger;
        this.prices = prices;
        this.budgetId = budgetId;
        this.limit = limit;
        this.effectiveLimit = limit.multiply(useFraction);
        this.maxPromptBytes = maxPromptBytes;
        this.events = events;
    }

    @Override
    public synchronized Reply generate(Request request) {
        PromptSizePolicy.requireFits(request, maxPromptBytes);
        BudgetBalance before;
        try {
            before = ledger.load(budgetId);
        } catch (RuntimeException ex) {
            return useLocal("BUDGET_UNAVAILABLE", request);
        }
        if (before.reviewRequired()) {
            return useLocal("REVIEW_REQUIRED", request);
        }
        BigDecimal reserved = prices.quote(request);
        if (before.chargedUsd().add(reserved).compareTo(effectiveLimit) > 0) {
            return useLocal("BUDGET_LIMIT", request);
        }
        try {
            ledger.save(new BudgetBalance(budgetId,
                    before.chargedUsd().add(reserved), true));
        } catch (RuntimeException ex) {
            return useLocal("RESERVATION_FAILED", request);
        }
        Reply reply;
        try {
            events.accept("OPENAI_CALL");
            reply = cloud.generate(request);
        } catch (RuntimeException ex) {
            return useLocal("OPENAI_ERROR", request);
        }
        if (reply == null || reply.text() == null || reply.text().isBlank()) {
            return useLocal("OPENAI_EMPTY", request);
        }
        var actual = prices.measured(reply);
        if (actual.isEmpty()) {
            events.accept("USAGE_OR_MODEL_UNKNOWN");
            return reply;
        }

        BigDecimal amount = actual.get();
        boolean review = amount.compareTo(reserved) > 0;

        try {
            ledger.save(new BudgetBalance(budgetId,
                    before.chargedUsd().add(amount), review));
            events.accept(review ? "QUOTE_EXCEEDED" : "OPENAI_ACCOUNTED");
        } catch (RuntimeException ex) {
            events.accept("SETTLEMENT_FAILED");
        }
        return reply;
    }

    private Reply useLocal(String reason, Request request) {
        events.accept(reason);
        return local.generate(request);
    }

    @Override
    public synchronized Status status() {
        try {
            var balance = ledger.load(budgetId);
            return new Status(budgetId, limit, effectiveLimit, balance.chargedUsd(),
                    effectiveLimit.subtract(balance.chargedUsd()).max(BigDecimal.ZERO),
                    balance.reviewRequired());
        } catch (RuntimeException ex) {
            throw new BusinessException(UNAVAILABLE, "Orcamento indisponivel.");
        }
    }
}