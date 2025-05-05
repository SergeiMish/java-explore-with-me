package ru.practicum.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    @NotBlank
    @Size(min = 1, max = 2000)
    private String text;
}