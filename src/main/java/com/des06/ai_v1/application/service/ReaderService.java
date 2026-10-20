package com.des06.ai_v1.application.service;

import com.des06.ai_v1.application.exception.BusinessException;
import com.des06.ai_v1.application.port.in.ReaderUseCase;
import com.des06.ai_v1.application.port.out.ReaderRepository;
import com.des06.ai_v1.domain.model.Reader;

import java.util.UUID;

import static com.des06.ai_v1.application.exception.BusinessException.Kind.CONFLICT;
import static com.des06.ai_v1.application.exception.BusinessException.Kind.NOT_FOUND;

public final class ReaderService implements ReaderUseCase {

    private final ReaderRepository readers;

    public ReaderService(ReaderRepository readers) { this.readers = readers; }
    @Override
    public Reader register(String name, String email) {
        Reader reader = new Reader(UUID.randomUUID().toString(), name, email);
        if (readers.existsByEmail(reader.email())) {
            throw new BusinessException(CONFLICT, "E-mail ja cadastrado.");
        }
        return readers.save(reader);
    }
    @Override
    public Reader get(String id) {
        return readers.findById(id).orElseThrow(() ->
                new BusinessException(NOT_FOUND, "Leitor nao encontrado."));
    }
}

