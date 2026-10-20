package com.des06.ai_v1.adapter.in.web.response;

import com.des06.ai_v1.domain.model.Reader;

public record ReaderResponse(String id, String name, String email) {
    static ReaderResponse from(Reader r) {
        return new ReaderResponse(r.id(), r.name(), r.email());
    }
}