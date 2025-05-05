package ru.practicum.service.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.practicum.model.enums.CommentStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private Long authorId;
    private String authorName;
    private Long eventId;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private CommentStatus status;
}