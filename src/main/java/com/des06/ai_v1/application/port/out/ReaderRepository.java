package com.des06.ai_v1.application.port.out;

import com.des06.ai_v1.domain.model.Reader;

import java.util.Optional;

public interface ReaderRepository {

    Reader save(Reader reader);
    Optional<Reader> findById(String id);
    boolean existsByEmail(String email);

}
