package com.des06.ai_v1.application.port.out;

public interface StudyAssistant {
    record Request(String system, String user) {}
    record Reply(String text, String provider, String model,
                 Integer inputTokens, Integer outputTokens,
                 Integer totalTokens, long durationMs) {}
    Reply generate(Request request);


}
