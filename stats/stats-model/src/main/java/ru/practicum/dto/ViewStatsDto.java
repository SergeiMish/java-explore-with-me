package ru.practicum.dto;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;


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