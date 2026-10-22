package com.des06.ai_v1;

import com.des06.ai_v1.domain.model.Book;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class BookTest {
    @Test
    void normalizesIsbnAndTopics() {
        var book = new Book("b1", "978-0-306-40615-7", " Livro ",
                " Autor ", Set.of(" Filosofia "), true);
        assertThat(book.isbn()).isEqualTo("9780306406157");
        assertThat(book.topics()).containsExactly("filosofia");
    }
    @Test
    void rejectsInvalidCheckDigit() {
        assertThatThrownBy(() -> new Book("b1", "9780306406158",
                "Livro", "Autor", Set.of("filosofia"), true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}