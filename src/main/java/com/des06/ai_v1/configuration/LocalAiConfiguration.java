package com.des06.ai_v1.configuration;

import com.des06.ai_v1.adapter.out.ai.OllamaStudyAssistant;
import com.des06.ai_v1.application.port.out.StudyAssistant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
@Profile("local | hybrid")
@EnableConfigurationProperties(LocalAiProperties.class)
public class LocalAiConfiguration {

    @Bean("localStudyAssistant")
    StudyAssistant localStudyAssistant(LocalAiProperties p) {

        var http = new SimpleClientHttpRequestFactory();
        http.setConnectTimeout(Duration.ofSeconds(5));
        http.setReadTimeout(Duration.ofSeconds(p.readTimeoutsSeconds()));

        var api = OllamaApi.builder()
                .baseUrl(p.baseUrl())
                .restClientBuilder(RestClient.builder().requestFactory(http))
                .build();

        var options = OllamaChatOptions.builder()
                .model(p.model())
                .numCtx(p.contextTokens())
                .numPredict(p.maxOutputTokens())
                .temperature(0.2)
                .keepAlive("5m")
                .build();

        var policy = RetryPolicy
                .builder()
                .maxRetries(1)
                .build();

        var retryTemplate = new RetryTemplate(policy);

        var model = OllamaChatModel.builder()
                .ollamaApi(api)
                .options(options)
                .retryTemplate(retryTemplate)
                .build();

        return new OllamaStudyAssistant(ChatClient.create(model),
                p.model(), p.maxPromptUtf8Bytes());
    }
}
