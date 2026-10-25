package com.des06.ai_v1.configuration;

import java.time.Duration;

import com.des06.ai_v1.adapter.ai.OpenAiStudyAssistant;
import com.des06.ai_v1.application.port.out.BudgetLedger;
import com.des06.ai_v1.application.port.out.StudyAssistant;
import com.des06.ai_v1.application.service.BudgetRoutingAssistant;
import com.des06.ai_v1.application.service.TextCostPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;

@Configuration
@Profile("hybrid")
@EnableConfigurationProperties({HybridAiProperties.class,
LocalAiProperties.class})
public class HybridAiConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(HybridAiConfiguration.class);


    @Bean
    @Primary
    BudgetRoutingAssistant budgetStudyAssistant(HybridAiProperties p,
                                                LocalAiProperties localProperties, BudgetLedger ledger,
                                                @Qualifier("localStudyAssistant") StudyAssistant local) {


        var options = OpenAiChatOptions.builder()
                .apiKey(p.apiKey())
                .baseUrl(p.baseUrl())
                .model(p.model())
                .maxCompletionTokens(p.maxOutputTokens())
                .reasoningEffort(p.reasoningEffort())
                .timeout(Duration.ofSeconds(60))
                .maxRetries(0)
                .build();

        var model = OpenAiChatModel.builder()
                .options(options)
                .build();

        StudyAssistant cloud = new OpenAiStudyAssistant(
                ChatClient.create(model),
                p.model()
        );

        var prices = new TextCostPolicy(p.model(), p.inputUsdPerMillion(),
                p.outputUsdPerMillion(), p.maxOutputTokens());
        return new BudgetRoutingAssistant(cloud, local, ledger, prices,
                p.budgetId(), p.limitUsd(), p.useFraction(),
                localProperties.maxPromptUtf8Bytes(),
                reason -> LOG.info("ai_route={}", reason));
    }
}
