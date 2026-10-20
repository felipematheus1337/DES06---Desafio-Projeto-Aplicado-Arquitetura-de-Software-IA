package com.des06.ai_v1.application.port.in;

import com.des06.ai_v1.domain.model.Reader;

public interface ReaderUseCase {

    Reader register(String name, String email);
    Reader get(String id);
}
