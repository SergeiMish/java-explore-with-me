package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    private Long id;
    @NotBlank
    private String name;
}
