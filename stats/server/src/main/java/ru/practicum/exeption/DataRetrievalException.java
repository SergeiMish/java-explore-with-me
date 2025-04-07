package ru.practicum.exeption;

public class DataRetrievalException extends RuntimeException {
    public DataRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}