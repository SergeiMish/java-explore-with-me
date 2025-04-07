package ru.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @Min(value = 0, message = "Hits count cannot be negative")
    private Long hits;
}