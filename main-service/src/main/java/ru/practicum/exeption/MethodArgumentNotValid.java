package ru.practicum.exeption;

import org.springframework.http.HttpStatus;

public class MethodArgumentNotValid extends MainServiceException {
    public MethodArgumentNotValid(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
