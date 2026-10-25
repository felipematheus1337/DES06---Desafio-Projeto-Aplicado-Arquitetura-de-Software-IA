package com.des06.ai_v1.application.service;


import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.port.out.StudyAssistant;

import java.nio.charset.StandardCharsets;

import static com.des06.ai_v1.application.exception.BusinessException.Kind.INVALID;


public final class PromptSizePolicy {

    private PromptSizePolicy() {}

    public static long utf8Bytes(StudyAssistant.Request request) {
        return (long) request.system().getBytes(StandardCharsets.UTF_8).length
                + request.user().getBytes(StandardCharsets.UTF_8).length;
    }

    public static void requireFits(StudyAssistant.Request request, int maxBytes) {
        if (request == null || request.system() == null || request.user() == null) {
            throw new BusinessException(INVALID, "Prompt incompleto.");
        }
        if (utf8Bytes(request) > maxBytes) {
            throw new BusinessException(INVALID,
                    "Contexto grande para o modelo local. Reduza trecho ou catalogo.");
        }
    }
}