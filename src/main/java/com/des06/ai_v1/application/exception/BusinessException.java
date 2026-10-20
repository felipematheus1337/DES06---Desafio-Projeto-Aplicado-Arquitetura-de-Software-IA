package com.des06.ai_v1.application.exception;

public final class BusinessException extends RuntimeException {

    public BusinessException(Kind kind, String message) {
        super(message);
        this.kind = kind;
    }

    public enum Kind { NOT_FOUND, CONFLICT, FORBIDDEN, INVALID, UNAVAILABLE };
    private final Kind kind;

    public Kind kind() { return kind;}

}
