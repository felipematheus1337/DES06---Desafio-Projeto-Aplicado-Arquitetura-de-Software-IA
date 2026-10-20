package com.des06.ai_v1.application.port.in;

import com.des06.ai_v1.application.model.StudyCommand;
import com.des06.ai_v1.application.model.StudyResult;

public interface StudyUseCase {
    StudyResult execute(StudyCommand command);
}
