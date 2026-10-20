package com.des06.ai_v1.adapter.in.web;

import com.des06.ai_v1.application.exception.BusinessException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ProblemDetail> business(BusinessException ex) {
        HttpStatus status = switch (ex.kind()) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case INVALID -> HttpStatus.BAD_REQUEST;
            case UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
        };
        return problem(status, ex.getMessage());
    }
    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<ProblemDetail> duplicate(DuplicateKeyException ex) {
        return problem(HttpStatus.CONFLICT, "Registro duplicado.");
    }
    @ExceptionHandler({MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class})
    ResponseEntity<ProblemDetail> invalidRequest(Exception ex) {
        return problem(HttpStatus.BAD_REQUEST,
                "Requisicao invalida. Confira os campos e seus limites.");
    }
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> invalidDomain(IllegalArgumentException ex) {
        return problem(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<ProblemDetail> database(DataAccessException ex) {
        return problem(HttpStatus.SERVICE_UNAVAILABLE,
                "Persistencia temporariamente indisponivel.");
    }
    private ResponseEntity<ProblemDetail> problem(HttpStatus status, String detail) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(status, detail);
        body.setTitle(status.getReasonPhrase());
        return ResponseEntity.status(status).body(body);
    }
}
