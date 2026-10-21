package com.des06.ai_v1.adapter.ai;

import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.port.out.StudyAssistant;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.des06.ai_v1.application.exception.BusinessException.Kind.UNAVAILABLE;

@Component
@Profile("!openai")
public class DisabledStudyAssistant implements StudyAssistant {

    @Override
    public Reply generate(Request request) {
        throw new BusinessException(UNAVAILABLE,
                "IA desativada. Ative o perfil openai para esta funcionalidade.");
    }
}
