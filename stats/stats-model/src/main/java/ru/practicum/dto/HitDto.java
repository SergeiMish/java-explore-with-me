package ru.practicum.dto;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HitDto {
    private String app;
    private String uri;
    private String ip;
    private Instant timestamp;
}
