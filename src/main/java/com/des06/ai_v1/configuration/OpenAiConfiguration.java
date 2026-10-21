package com.des06.ai_v1.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("openai")
public class OpenAiConfiguration {

    @Bean
    ChatClient readingChatClient(ChatClient.Builder builder) {
        return builder.build();
    }


}
