package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.interfaces.StatsRepository;
import ru.practicum.model.Hit;


@Service
@RequiredArgsConstructor
@Transactional
public class StatsService {

    private final StatsRepository statsRepository;

    public void saveHit(Hit hit){
        statsRepository.save(hit);
    }
}
