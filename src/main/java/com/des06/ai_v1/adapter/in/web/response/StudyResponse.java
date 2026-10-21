package com.des06.ai_v1.adapter.in.web.response;

import java.util.List;

public record StudyResponse(String requestId, String answer,
                            String provider, String model, Integer inputTokens,
                            Integer outputTokens, Integer totalTokens,
                            long durationMs, boolean usageRecorded, List<String> contextBookIds) {}