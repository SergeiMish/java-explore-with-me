package ru.practicum.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime created;

    private Long event;
    private Long id;
    private Long requester;

    @Enumerated(EnumType.STRING)
    private ParticipationRequestStatus status;

    public enum ParticipationRequestStatus {
        PENDING, CONFIRMED, REJECTED, CANCELED
    }
}
