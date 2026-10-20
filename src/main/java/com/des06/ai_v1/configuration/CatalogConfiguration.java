package com.des06.ai_v1.configuration;

import com.des06.ai_v1.application.port.in.CatalogUseCase;
import com.des06.ai_v1.application.port.out.BookRepository;
import com.des06.ai_v1.application.service.CatalogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfiguration {

    @Bean
    CatalogUseCase catalogUseCase(BookRepository books) {
        return new CatalogService(books);
    }


}
