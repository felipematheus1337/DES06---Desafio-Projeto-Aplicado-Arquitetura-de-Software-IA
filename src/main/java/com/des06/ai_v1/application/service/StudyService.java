package com.des06.ai_v1.application.service;

import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.model.StudyCommand;
import com.des06.ai_v1.application.model.StudyResult;
import com.des06.ai_v1.application.model.UsageEvent;
import com.des06.ai_v1.application.port.in.StudyUseCase;
import com.des06.ai_v1.application.port.out.*;
import com.des06.ai_v1.domain.model.Book;
import com.des06.ai_v1.domain.model.StudyTask;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.des06.ai_v1.application.exception.BusinessException.Kind.*;

public final class StudyService implements StudyUseCase {
    private final ReaderRepository readers;
    private final BookRepository books;
    private final PurchaseRepository purchases;
    private final StudyAssistant assistant;
    private final UsageRecorder usage;
    private final StudyPromptComposer prompts;
    private final Clock clock;

    public StudyService(ReaderRepository readers, BookRepository books,
                        PurchaseRepository purchases, StudyAssistant assistant,
                        UsageRecorder usage, StudyPromptComposer prompts, Clock clock) {
        this.readers = readers;
        this.books = books;
        this.purchases = purchases;
        this.assistant = assistant;
        this.usage = usage;
        this.prompts = prompts;
        this.clock = clock;
    }

    @Override
    public StudyResult execute(StudyCommand c) {
        validate(c);

        readers.findById(c.readerId()).orElseThrow(() ->
                new BusinessException(NOT_FOUND, "Leitor nao encontrado."));

        Book base = books.findById(c.bookId()).orElseThrow(() ->
                new BusinessException(NOT_FOUND, "Livro nao encontrado."));

        if (!purchases.exists(c.readerId(), c.bookId())) {
            throw new BusinessException(FORBIDDEN, "Livro nao adquirido pelo leitor.");
        }

        List<Book> candidates = c.task() == StudyTask.EXPLAIN
                ? List.of() : books.findActiveByTopic(c.topic(), 10);

        if (c.task() != StudyTask.EXPLAIN && candidates.isEmpty()) {
            throw new BusinessException(INVALID, "Catalogo sem obras para o assunto.");
        }

        String requestId = UUID.randomUUID().toString();

        StudyAssistant.Reply reply = assistant.generate(
                prompts.compose(c, base, candidates));

        boolean recorded = usage.record(new UsageEvent(
                requestId, c.readerId(), c.bookId(), c.task(),
                reply.provider(), reply.model(), reply.inputTokens(),
                reply.outputTokens(), reply.totalTokens(),
                reply.durationMs(), Instant.now(clock)));

        return new StudyResult(requestId, reply.text(), reply.provider(),
                reply.model(), reply.inputTokens(), reply.outputTokens(),
                reply.totalTokens(), reply.durationMs(), recorded,
                candidates.stream().map(Book::id).toList());
    }

    private void validate(StudyCommand c) {
        if (c == null || blank(c.readerId()) || blank(c.bookId()) || c.task() == null) {
            throw new BusinessException(INVALID, "Leitor, livro e tarefa obrigatorios.");
        }
        if (length(c.question()) > 1000 || length(c.excerpt()) > 6000
                || length(c.topic()) > 80) {
            throw new BusinessException(INVALID, "Entrada acima do limite.");
        }
        if (c.task() == StudyTask.EXPLAIN && blank(c.excerpt())) {
            throw new BusinessException(INVALID, "Informe o trecho a explicar.");
        }
        if (c.task() != StudyTask.EXPLAIN && blank(c.topic())) {
            throw new BusinessException(INVALID, "Informe o assunto.");
        }
        if (c.task() == StudyTask.PLAN && (c.weeks() < 1 || c.weeks() > 12
                || c.minutesPerDay() < 10 || c.minutesPerDay() > 120)) {
            throw new BusinessException(INVALID, "Plano fora dos limites permitidos.");
        }
    }

    private boolean blank(String s) { return s == null || s.isBlank(); }

    private int length(String s) { return s == null ? 0 : s.length(); }
}
