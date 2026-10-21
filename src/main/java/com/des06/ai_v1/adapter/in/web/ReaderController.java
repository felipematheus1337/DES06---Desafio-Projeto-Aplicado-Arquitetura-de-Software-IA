package com.des06.ai_v1.adapter.in.web;

import com.des06.ai_v1.adapter.in.web.request.ReaderRequest;
import com.des06.ai_v1.adapter.in.web.response.ReaderResponse;
import com.des06.ai_v1.application.port.in.ReaderUseCase;
import com.des06.ai_v1.domain.model.Reader;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/readers")
public class ReaderController {
    private final ReaderUseCase readers;

    public ReaderController(ReaderUseCase readers) { this.readers = readers; }

    @PostMapping
    public ResponseEntity<ReaderResponse> register(
            @Valid @RequestBody ReaderRequest r) {
        Reader reader = readers.register(r.name(), r.email());
        return ResponseEntity.created(URI.create("/api/readers/" + reader.id()))
                .body(ReaderResponse.from(reader));
    }
    @GetMapping("/{id}")
    public ReaderResponse get(@PathVariable String id) {
        return ReaderResponse.from(readers.get(id));
    }
}
