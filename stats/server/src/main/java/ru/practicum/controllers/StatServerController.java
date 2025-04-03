package ru.practicum.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatServerController {

    @GetMapping
    public String test(){
        return "Работает";
    }
}
