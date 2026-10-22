package com.des06.ai_v1.adapter.in.web.request;

import com.des06.ai_v1.domain.model.StudyTask;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudyRequest(
        @NotBlank String readerId, @NotBlank String bookId,
        @NotNull StudyTask task, @Size(max = 1000) String question,
        @Size(max = 6000) String excerpt, @Nullable @Size(max = 80) String topic,
        @Nullable Integer weeks, @Nullable Integer minutesPerDay) {}
