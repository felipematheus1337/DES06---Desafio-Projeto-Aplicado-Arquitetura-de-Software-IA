package com.des06.ai_v1.configuration;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("leitura.ai.hybrid")
public record HybridAiProperties(
        @NotBlank String apiKey,
        @NotBlank String baseUrl,
        @NotBlank String model,
        @Min(1) int maxOutputTokens,
        @NotBlank String reasoningEffort,
        @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal inputUsdPerMillion,
        @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal outputUsdPerMillion,
        @NotBlank String budgetId,
        @NotNull @DecimalMin("0") BigDecimal limitUsd,
        @NotNull @DecimalMin(value = "0", inclusive = false)
        @DecimalMax("1") BigDecimal useFraction) {}

