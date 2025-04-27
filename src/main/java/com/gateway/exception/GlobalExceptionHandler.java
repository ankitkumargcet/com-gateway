package com.gateway.exception;

import com.gateway.model.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ErrorResponse handleResponseStatusException(ResponseStatusException ex) {
        return ErrorResponse.builder()
                .code(ex.getStatusCode().value())
                .message(ex.getReason())
                .timestamp(Instant.now())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse handleGeneralException(Exception ex) {
        return ErrorResponse.builder()
                .code(0)
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();
    }

}
