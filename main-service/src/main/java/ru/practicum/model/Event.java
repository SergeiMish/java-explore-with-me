package ru.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 20, max = 2000)
    @Column(columnDefinition = "TEXT")
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "event_date")
    @Future
    private LocalDateTime eventDate;

    @Embedded
    private Location location;

    @Builder.Default
    private Boolean paid = false;

    @Column(name = "participant_limit")
    @Builder.Default
    private Integer participantLimit = 0;

    @Column(name = "request_moderation")
    @Builder.Default
    private Boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User initiator;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 20)
    private EventState state = EventState.PENDING;

    @Column(name = "created_on")
    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ParticipationRequest> requests = new ArrayList<>();

    @ManyToMany(mappedBy = "events")
    @Builder.Default
    private Set<Compilation> compilations = new HashSet<>();

    @Transient
    private Long views;
}