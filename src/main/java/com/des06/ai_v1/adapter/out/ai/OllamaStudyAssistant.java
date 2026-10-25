package com.des06.ai_v1.adapter.out.ai;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.port.out.StudyAssistant;
import com.des06.ai_v1.application.service.PromptSizePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;


import static com.des06.ai_v1.application.exception.BusinessException.Kind.UNAVAILABLE;

public final class OllamaStudyAssistant implements StudyAssistant {

    private static final Logger LOG =
            LoggerFactory.getLogger(OllamaStudyAssistant.class);

    private final ChatClient chat;
    private final String model;
    private final int maxPromptBytes;

    public OllamaStudyAssistant(ChatClient chat, String model, int maxPromptBytes) {
        this.chat = chat;
        this.model = model;
        this.maxPromptBytes = maxPromptBytes;
    }

    @Override
    public Reply generate(Request request) {

        PromptSizePolicy.requireFits(request, maxPromptBytes);

        long started = System.nanoTime();
        ChatResponse response;

        try {
            var prompt = new Prompt(List.of(new SystemMessage(request.system()),
                    new UserMessage(request.user())));

            response = chat.prompt(prompt)
                    .call()
                    .chatResponse();

        } catch (RuntimeException ex) {
            LOG.warn("Falha local: {}", ex.getClass().getSimpleName());
            throw new BusinessException(UNAVAILABLE,
                    "Modelo local indisponivel. Confira Ollama e modelo baixado.");
        }

        if (response == null || response.getResult() == null
                || response.getResult().getOutput().getText() == null
                || response.getResult().getOutput().getText().isBlank()) {
            throw new BusinessException(UNAVAILABLE, "Modelo local sem resposta.");
        }

        var usage = response.getMetadata().getUsage();

        boolean measured = usage != null && usage.getTotalTokens() != null
                && usage.getTotalTokens() > 0;

        String returnedModel = response.getMetadata().getModel();
        long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - started);

        return new Reply(response.getResult().getOutput().getText(), "OLLAMA",
                returnedModel == null || returnedModel.isBlank() ? model : returnedModel,
                measured ? usage.getPromptTokens() : null,
                measured ? usage.getCompletionTokens() : null,
                measured ? usage.getTotalTokens() : null, elapsed);
    }

}