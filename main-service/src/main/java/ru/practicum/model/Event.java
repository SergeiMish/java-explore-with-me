package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "events")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Embedded
    private Location location;

    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User initiator;

    @OneToMany(mappedBy = "event")
    private List<ParticipationRequest> requests;

    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;
}
