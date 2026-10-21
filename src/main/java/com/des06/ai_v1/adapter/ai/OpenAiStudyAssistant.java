package com.des06.ai_v1.adapter.ai;

import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.port.out.StudyAssistant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.des06.ai_v1.application.exception.BusinessException.Kind.UNAVAILABLE;

@Component
@Profile("openai")
public class OpenAiStudyAssistant implements StudyAssistant {

    private static final Logger LOG = LoggerFactory.getLogger(OpenAiStudyAssistant.class);
    public static final String OPENAI = "OPENAI";
    private final ChatClient chat;
    private final String configuredModel;

    public OpenAiStudyAssistant(ChatClient chat,
                                @Value("${spring.ai.openai.chat.options.model}") String configuredModel) {
        this.chat = chat;
        this.configuredModel = configuredModel;
    }

    @Override
    public Reply generate(Request request) {
        long started = System.nanoTime();
        ChatResponse response;

        try {

            var prompt = new Prompt(
                    List.of(new SystemMessage(request.system()),
                            new UserMessage(request.user())));
            response = chat.prompt(prompt).call().chatResponse();

        } catch (RuntimeException ex) {

            LOG.warn("Falha no provedor OpenAI: {}", ex.getClass().getSimpleName());
            throw new BusinessException(UNAVAILABLE, "Provedor de IA indisponível. Tente novamente mais tarde");
        }

        isResponseValid(response);

        Usage usage = response.getMetadata().getUsage();
        boolean measured = usage != null && usage.getTotalTokens() != null
                && usage.getTotalTokens() > 0;

        String model = response.getMetadata().getModel();
        long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - started);


        return new Reply(response.getResult().getOutput().getText(), OPENAI,
                model == null || model.isBlank() ? configuredModel : model,
                measured ? usage.getPromptTokens() : null,
                measured ? usage.getCompletionTokens() : null,
                measured ? usage.getTotalTokens() : null, elapsed);

    }

    private static void isResponseValid(ChatResponse response) {
        if (response == null || response.getResult() == null
        || response.getResult().getOutput().getText() == null
        || response.getResult().getOutput().getText().isBlank()) {
            throw new BusinessException(UNAVAILABLE, "Provedor retornou resposta vazia.");
        }
    }
}
