package com.des06.ai_v1.configuration;

import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;
import org.springframework.core.env.Environment;

@Configuration
public class AiProfileGuard {

    public AiProfileGuard(Environment environment) {
        var modes = Set.of("openai", "local", "hybrid");
        long count = Arrays.stream(environment.getActiveProfiles())
                .filter(modes::contains).count();

        if (count > 1) {
            throw new IllegalStateException(
                    "Ative somente um perfil de IA: openai, local ou hybrid.");
        }
    }
}
