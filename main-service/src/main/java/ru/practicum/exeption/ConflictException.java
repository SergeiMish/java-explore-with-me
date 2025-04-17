package ru.practicum.exeption;

import org.springframework.http.HttpStatus;

public class ConflictException extends MainServiceException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}