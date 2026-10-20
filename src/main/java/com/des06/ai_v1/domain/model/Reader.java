package com.des06.ai_v1.domain.model;

import java.util.Locale;

public record Reader(String id, String name, String email) {
    public Reader {
        if (name == null || name.isBlank()
                || email == null || email.isBlank()) {
            throw new IllegalArgumentException("Dados do leitor incompletos.");
        }
        name = name.strip();
        email = email.strip().toLowerCase(Locale.ROOT);
    }
}

