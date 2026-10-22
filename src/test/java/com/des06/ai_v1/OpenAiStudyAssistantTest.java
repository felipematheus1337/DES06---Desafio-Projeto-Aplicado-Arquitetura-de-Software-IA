package com.des06.ai_v1;

import java.util.List;

import com.des06.ai_v1.adapter.ai.OpenAiStudyAssistant;
import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.port.out.StudyAssistant;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.DefaultUsage;
import org.springframework.ai.chat.model.*;
import org.springframework.ai.chat.prompt.Prompt;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OpenAiStudyAssistantTest {


    @Test
    void providerFailureBecomesControlledUnavailableError() {
        ChatModel model = mock(ChatModel.class);
        when(model.call(any(Prompt.class))).thenThrow(new RuntimeException("erro"));
        var adapter = new OpenAiStudyAssistant(ChatClient.create(model), "modelo");
        assertThatThrownBy(() -> adapter.generate(
                new StudyAssistant.Request("Sistema", "Pergunta")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Provedor de IA indisponível. Tente novamente mais tarde");
    }
}