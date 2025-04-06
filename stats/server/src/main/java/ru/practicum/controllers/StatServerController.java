package ru.practicum.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.model.Hit;
import ru.practicum.service.StatsService;

@RestController
@RequestMapping("/hit")
@RequiredArgsConstructor
public class StatServerController {

    private final StatsService statsService;

    @PostMapping
    public ResponseEntity<Hit> saveHit(@RequestBody Hit hit){
        statsService.saveHit(hit);
    return ResponseEntity.status(HttpStatus.CREATED).body(hit);
    }
}
