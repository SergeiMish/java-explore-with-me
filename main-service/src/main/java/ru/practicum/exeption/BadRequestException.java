package ru.practicum.exeption;

import org.springframework.http.HttpStatus;

public class BadRequestException extends MainServiceException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}