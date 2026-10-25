package com.des06.ai_v1.configuration;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("leitura.ai.local")
public record LocalAiProperties(
        @NotBlank String baseUrl,
        @NotBlank String model,
        @Min(1024) int contextTokens,
        @Min(1) int maxOutputTokens,
        @Min(1) int maxPromptUtf8Bytes,
        @Min(1) int readTimeoutsSeconds) {

    @AssertTrue(message = "Entrada, margem e saida devem caber no contexto local")

    public boolean isContextBudgetConsistent() {
        return (long) maxPromptUtf8Bytes + 256 + maxOutputTokens <= contextTokens;
    }
}