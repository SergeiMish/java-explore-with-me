package ru.practicum.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exeption.StatsClientException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsClient {
    @Value("${stats.server.url}")
    private String serverUrl;
    private final RestTemplate restTemplate;

    public void saveHit(EndpointHitDto endpointHitDto) {
        String url = serverUrl + "/hit";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<EndpointHitDto> request = new HttpEntity<>(endpointHitDto, headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    Void.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new StatsClientException("Не удалось сохранить информацию о запросе. Код ошибки: " +
                        response.getStatusCode().value());
            }
        } catch (RestClientException ex) {
            throw new StatsClientException("Ошибка при обращении к сервису статистики: " + ex.getMessage(), ex);
        }
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                       List<String> uris, boolean unique) {
        String url = UriComponentsBuilder.fromHttpUrl(serverUrl + "/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", uris != null ? String.join(",", uris) : "")
                .queryParam("unique", unique)
                .toUriString();

        try {
            ResponseEntity<List<ViewStatsDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new StatsClientException("Не удалось получить статистику. Код ошибки: " +
                        response.getStatusCode().value());
            }

            return response.getBody();
        } catch (RestClientException ex) {
            throw new StatsClientException("Ошибка при получении статистики: " + ex.getMessage(), ex);
        }
    }
}