package ru.practicum.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exeption.ValidationException;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class StatsServerController {

    private final StatsService statsService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    public ResponseEntity<Void> saveHit(@RequestBody @Valid EndpointHitDto hitDto) {
        statsService.saveHit(hitDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {

        log.info("Request received - start: {}, end: {}, uris: {}, unique: {}",
                start, end, uris, unique);

        LocalDateTime startDate = parseDateTime(start);
        LocalDateTime endDate = parseDateTime(end);

        return statsService.getStats(startDate, endDate, uris, unique);
    }

    private LocalDateTime parseDateTime(String dateStr) {
        try {
            String normalized = dateStr.replace("%20", " ");
            return LocalDateTime.parse(normalized, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ValidationException(
                    "Invalid date format: '" + dateStr + "'. Required format: yyyy-MM-dd HH:mm:ss");
        }
    }
}