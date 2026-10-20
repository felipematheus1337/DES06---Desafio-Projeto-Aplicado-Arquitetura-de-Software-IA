package com.des06.ai_v1.application.model;

import com.des06.ai_v1.domain.model.StudyTask;

public record StudyCommand(String readerId, String bookId, StudyTask task,
                           String question, String excerpt, String topic,
                           int weeks, int minutesPerDay) {}

