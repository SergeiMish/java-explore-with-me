package ru.practicum.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsClientControllers {

    @GetMapping
    public String test() {
        return "Клиентский сервис на месте";
    }
}
