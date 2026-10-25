package com.des06.ai_v1.application.service;

import com.des06.ai_v1.application.port.out.StudyAssistant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public final class TextCostPolicy {

    private static final BigDecimal MILLION = new BigDecimal("1000000");
    private final String model;
    private final BigDecimal inputPrice;
    private final BigDecimal outputPrice;
    private final int maxOutputTokens;


    public TextCostPolicy(String model, BigDecimal inputPrice,
                          BigDecimal outputPrice, int maxOutputTokens) {
        if (model == null || model.isBlank() || inputPrice.signum() <= 0
                || outputPrice.signum() <= 0 || maxOutputTokens < 1) {
            throw new IllegalArgumentException("Modelo, tarifas ou limite invalidos.");
        }
        this.model = model;
        this.inputPrice = inputPrice;
        this.outputPrice = outputPrice;
        this.maxOutputTokens = maxOutputTokens;
    }


    public BigDecimal quote(StudyAssistant.Request request) {
        long inputBound = PromptSizePolicy.utf8Bytes(request) + 256;
        return cost(inputBound, maxOutputTokens);
    }

    public Optional<BigDecimal> measured(StudyAssistant.Reply reply) {
        if (reply == null || !"OPENAI".equals(reply.provider())
                || reply.inputTokens() == null || reply.outputTokens() == null
                || reply.inputTokens() < 0 || reply.outputTokens() < 0
                || (long) reply.inputTokens() + reply.outputTokens() == 0
                || reply.model() == null
                || !(reply.model().equals(model) || reply.model().startsWith(model + "-"))) {
            return Optional.empty();
        }
        return Optional.of(cost(reply.inputTokens(), reply.outputTokens()));
    }


    private BigDecimal cost(long input, long output) {
        return inputPrice.multiply(BigDecimal.valueOf(input))
                .add(outputPrice.multiply(BigDecimal.valueOf(output)))
                .divide(MILLION, 9, RoundingMode.CEILING);
    }
}