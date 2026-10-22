package com.des06.ai_v1;

import java.util.Set;

import com.des06.ai_v1.adapter.in.web.ApiExceptionHandler;
import com.des06.ai_v1.adapter.in.web.BookController;
import com.des06.ai_v1.application.port.in.CatalogUseCase;
import com.des06.ai_v1.domain.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

    @Test
    void createsBookWithLocationHeader() throws Exception {
        var catalog = mock(CatalogUseCase.class);
        when(catalog.create(any(), any(), any(), any())).thenReturn(new Book(
                "b1", "9780306406157", "Livro", "Autor",
                Set.of("filosofia"), true));
        var mvc = MockMvcBuilders.standaloneSetup(new BookController(catalog))
                .setControllerAdvice(new ApiExceptionHandler()).build();
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("""
 {"isbn":"9780306406157","title":"Livro",
 "author":"Autor","topics":["filosofia"]}
 """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/books/b1"))
                .andExpect(jsonPath("$.id").value("b1"));
    }

    @Test
    void emptyTitleIsRejectedBeforeUseCase() throws Exception {
        var catalog = mock(CatalogUseCase.class);
        var mvc = MockMvcBuilders.standaloneSetup(new BookController(catalog))
                .setControllerAdvice(new ApiExceptionHandler()).build();
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("""
 {"isbn":"9780306406157","title":"",
 "author":"Autor","topics":["filosofia"]}
 """))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(catalog);
    }
}

