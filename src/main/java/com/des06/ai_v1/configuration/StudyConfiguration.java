package com.des06.ai_v1.configuration;

import com.des06.ai_v1.application.port.in.StudyUseCase;
import com.des06.ai_v1.application.port.out.*;
import com.des06.ai_v1.application.service.StudyPromptComposer;
import com.des06.ai_v1.application.service.StudyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;

@Configuration
public class StudyConfiguration {

    @Bean
    StudyPromptComposer studyPromptComposer(
            @Value("classpath:prompts/study-system.txt") Resource resource)
            throws IOException {
        return new StudyPromptComposer(
                resource.getContentAsString(StandardCharsets.UTF_8));
    }
    @Bean
    StudyUseCase studyUseCase(ReaderRepository readers, BookRepository books,
                              PurchaseRepository purchases, StudyAssistant assistant,
                              UsageRecorder usage, StudyPromptComposer prompts, Clock clock) {
        return new StudyService(readers, books, purchases,
                assistant, usage, prompts, clock);
    }
}
