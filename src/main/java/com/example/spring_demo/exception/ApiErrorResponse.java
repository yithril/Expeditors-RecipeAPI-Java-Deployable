package com.example.spring_demo.exception;

import java.util.List;

public class ApiErrorResponse {

    private final int status;
    private final String message;
    private final List<FieldError> errors;

    public ApiErrorResponse(int status, String message, List<FieldError> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public record FieldError(String field, String message) {
    }
}
