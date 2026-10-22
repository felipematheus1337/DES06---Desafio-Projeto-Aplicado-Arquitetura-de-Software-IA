package com.des06.ai_v1;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.openai.autoconfigure.*;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import static org.assertj.core.api.Assertions.*;
class OpenAiDisabledConfigurationTest {

    @Test
    void disabledModelsDoNotRequireAnApiKey() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        OpenAiChatAutoConfiguration.class,
                        OpenAiEmbeddingAutoConfiguration.class,
                        OpenAiImageAutoConfiguration.class,
                        OpenAiAudioSpeechAutoConfiguration.class,
                        OpenAiAudioTranscriptionAutoConfiguration.class,
                        OpenAiModerationAutoConfiguration.class))
                .withPropertyValues(
                        "spring.ai.model.chat=none",
                        "spring.ai.model.embedding=none",
                        "spring.ai.model.image=none",
                        "spring.ai.model.moderation=none",
                        "spring.ai.model.audio.speech=none",
                        "spring.ai.model.audio.transcription=none")
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    assertThat(context).doesNotHaveBean(ChatModel.class);
                });
    }
}
