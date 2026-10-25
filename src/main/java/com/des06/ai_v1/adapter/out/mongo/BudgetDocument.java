package com.des06.ai_v1.adapter.out.mongo;

import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document("ai_budget")
public record BudgetDocument(@Id String id,
                             @Field(targetType = FieldType.DECIMAL128) BigDecimal chargedUsd,
                             boolean reviewRequired, Instant updatedAt) {}