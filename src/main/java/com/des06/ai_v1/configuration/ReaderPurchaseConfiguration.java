package com.des06.ai_v1.configuration;
import java.time.Clock;

import com.des06.ai_v1.application.port.in.CatalogUseCase;
import com.des06.ai_v1.application.port.in.PurchaseUseCase;
import com.des06.ai_v1.application.port.in.ReaderUseCase;
import com.des06.ai_v1.application.port.out.PurchaseRepository;
import com.des06.ai_v1.application.port.out.ReaderRepository;
import com.des06.ai_v1.application.service.PurchaseService;
import com.des06.ai_v1.application.service.ReaderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReaderPurchaseConfiguration {
    @Bean
    Clock clock() { return Clock.systemUTC(); }

    @Bean
    ReaderUseCase readerUseCase(ReaderRepository readers) {
        return new ReaderService(readers);
    }

    @Bean
    PurchaseUseCase purchaseUseCase(ReaderUseCase readers, CatalogUseCase catalog,
                                    PurchaseRepository purchases, Clock clock) {
        return new PurchaseService(readers, catalog, purchases, clock);
    }
}
