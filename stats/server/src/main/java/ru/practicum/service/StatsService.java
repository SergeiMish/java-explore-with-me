package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exeption.DataRetrievalException;
import ru.practicum.exeption.ValidationException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.HitRepository;
import ru.practicum.repository.ViewStatsProjection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {
    private final HitRepository hitRepository;
    private final StatsMapper statsMapper;

    public void saveHit(EndpointHitDto endpointHitDto) {
        try {
            Hit hit = statsMapper.toHit(endpointHitDto);
            hitRepository.save(hit);
            log.info("Hit saved: {}", hit);
        } catch (Exception e) {
            log.error("Failed to save hit: {}", endpointHitDto, e);
            throw new DataRetrievalException("Failed to save hit", e);
        }
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                       List<String> uris, boolean unique) {
        validateDates(start, end);

        List<ViewStatsProjection> stats = getStatsFromRepository(start, end, uris, unique);

        return stats.stream()
                .map(statsMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }

    private List<ViewStatsProjection> getStatsFromRepository(LocalDateTime start, LocalDateTime end,
                                                             List<String> uris, boolean unique) {
        try {
            long startTime = System.currentTimeMillis();
            List<ViewStatsProjection> result;

            if (unique) {
                result = uris == null || uris.isEmpty() ?
                        hitRepository.findUniqueStats(start, end) :
                        hitRepository.findUniqueStatsByUris(start, end, uris);
            } else {
                result = uris == null || uris.isEmpty() ?
                        hitRepository.findStats(start, end) :
                        hitRepository.findStatsByUris(start, end, uris);
            }

            long duration = System.currentTimeMillis() - startTime;
            log.debug("Запрос к БД выполнен за {} мс. Найдено {} записей", duration, result.size());
            return result;

        } catch (Exception e) {
            log.error("Ошибка при запросе статистики из БД. Параметры: start={}, end={}, uris={}, unique={}",
                    start, end, uris, unique, e);
            throw new DataRetrievalException("Failed to retrieve statistics", e);
        }
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new ValidationException("Start and end dates must not be null");
        }
        if (start.isAfter(end)) {
            throw new ValidationException("Start date must be before end date");
        }
    }
}