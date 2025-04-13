package ru.practicum.exeption;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends MainServiceException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}