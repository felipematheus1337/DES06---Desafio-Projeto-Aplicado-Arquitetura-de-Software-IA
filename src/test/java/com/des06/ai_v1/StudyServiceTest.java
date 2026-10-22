package com.des06.ai_v1;

import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.model.StudyCommand;
import com.des06.ai_v1.application.port.out.*;
import com.des06.ai_v1.application.service.StudyPromptComposer;
import com.des06.ai_v1.application.service.StudyService;
import com.des06.ai_v1.domain.model.Book;
import com.des06.ai_v1.domain.model.Reader;
import com.des06.ai_v1.domain.model.StudyTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class StudyServiceTest {

    ReaderRepository readers = mock(ReaderRepository.class);
    BookRepository books = mock(BookRepository.class);
    PurchaseRepository purchases = mock(PurchaseRepository.class);
    StudyAssistant assistant = mock(StudyAssistant.class);
    UsageRecorder usage = mock(UsageRecorder.class);
    StudyService service;

    @BeforeEach
    void prepare() {
        service = new StudyService(readers, books, purchases, assistant,
                usage, new StudyPromptComposer("Sistema"), Clock.systemUTC());

        when(readers.findById("r1")).thenReturn(
                Optional.of(new Reader("r1", "Leitor", "a@example.com")));
        when(books.findById("b1")).thenReturn(Optional.of(new Book(
                "b1", "9780306406157", "Livro", "Autor",
                Set.of("filosofia"), true)));
    }
    private StudyCommand explain() {
        return new StudyCommand("r1", "b1", StudyTask.EXPLAIN,
                "Explique", "Um trecho de estudo.", null, 0, 0);
    }

    @Test
    void purchaseIsCheckedBeforeAnyPaidCall() {
        assertThatThrownBy(() -> service.execute(explain()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("nao adquirido");
        verifyNoInteractions(assistant, usage);
    }
    @Test
    void blankExcerptNeverReachesProvider() {
        var command = new StudyCommand("r1", "b1", StudyTask.EXPLAIN,
                null, " ", null, 0, 0);
        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(BusinessException.class);
        verifyNoInteractions(assistant, usage);
    }
    @Test
    void successRecordsReturnedUsage() {
        when(purchases.exists("r1", "b1")).thenReturn(true);
        when(assistant.generate(any())).thenReturn(new StudyAssistant.Reply(
                "Resposta", "OPENAI", "modelo-teste", 100, 20, 120, 15));
        when(usage.record(any())).thenReturn(true);
        var result = service.execute(explain());
        assertThat(result.totalTokens()).isEqualTo(120);
        assertThat(result.usageRecorded()).isTrue();
        verify(usage).record(argThat(e -> e.inputTokens() == 100
                && e.outputTokens() == 20 && e.requestId() != null));
    }
    @Test
    void emptyCatalogDoesNotSpendTokens() {
        when(purchases.exists("r1", "b1")).thenReturn(true);
        when(books.findActiveByTopic("filosofia", 10)).thenReturn(List.of());
        var command = new StudyCommand("r1", "b1", StudyTask.RECOMMEND,
                null, null, "filosofia", 0, 0);
        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Catalogo");
        verifyNoInteractions(assistant, usage);
    }
    @Test
    void auditFailureDoesNotRepeatAlreadyCompletedCall() {
        when(purchases.exists("r1", "b1")).thenReturn(true);
        when(assistant.generate(any())).thenReturn(new StudyAssistant.Reply(
                "Resposta", "OPENAI", "modelo-teste", 100, 20, 120, 15));
        when(usage.record(any())).thenReturn(false);
        assertThat(service.execute(explain()).usageRecorded()).isFalse();
        verify(assistant, times(1)).generate(any());
    }
}
