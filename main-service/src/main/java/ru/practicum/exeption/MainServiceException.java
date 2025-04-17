package ru.practicum.exeption;

import org.springframework.http.HttpStatus;

public class MainServiceException extends RuntimeException {
    private final HttpStatus status;

    public MainServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}