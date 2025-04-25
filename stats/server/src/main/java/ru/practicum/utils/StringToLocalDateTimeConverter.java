package ru.practicum.utils;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        String normalized = source.replace("%20", " ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            return LocalDateTime.parse(normalized, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid date format. Required format: yyyy-MM-dd HH:mm:ss");
        }
    }
}