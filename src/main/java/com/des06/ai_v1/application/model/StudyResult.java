package com.des06.ai_v1.application.model;

import java.util.List;

public record StudyResult(String requestId, String answer,
                          String provider, String model, Integer inputTokens,
                          Integer outputTokens, Integer totalTokens,
                          long durationMs, boolean usageRecorded,
                          List<String> contextBookIds) {}