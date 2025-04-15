package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.model.enums.RequestStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;

    private LocalDateTime created = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}