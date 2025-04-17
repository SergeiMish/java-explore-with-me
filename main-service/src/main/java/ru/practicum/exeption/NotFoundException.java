package ru.practicum.exeption;

import org.springframework.http.HttpStatus;

public class NotFoundException extends MainServiceException {
    public NotFoundException(String entity) {
        super(entity + " not found", HttpStatus.NOT_FOUND);
    }
}