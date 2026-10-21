package com.des06.ai_v1.adapter.in.web;

import com.des06.ai_v1.adapter.in.web.request.StudyRequest;
import com.des06.ai_v1.adapter.in.web.response.StudyResponse;
import com.des06.ai_v1.application.model.StudyCommand;
import com.des06.ai_v1.application.model.StudyResult;
import com.des06.ai_v1.application.port.in.StudyUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/study")
public class StudyController {
    private final StudyUseCase study;
    public StudyController(StudyUseCase study) { this.study = study; }

    @PostMapping
    public StudyResponse answer(@Valid @RequestBody StudyRequest r) {
        StudyResult s = study.execute(new StudyCommand(r.readerId(), r.bookId(),
                r.task(), r.question(), r.excerpt(), r.topic(),
                r.weeks(), r.minutesPerDay()));

        return new StudyResponse(s.requestId(), s.answer(), s.provider(),
                s.model(), s.inputTokens(), s.outputTokens(), s.totalTokens(),
                s.durationMs(), s.usageRecorded(), s.contextBookIds());
    }
}

