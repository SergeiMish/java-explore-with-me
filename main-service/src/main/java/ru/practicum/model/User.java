package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    String name;
    @OneToMany(mappedBy = "initiator")
    private List<Event> organizedEvents = new ArrayList<>();

    @OneToMany(mappedBy = "requester")
    private List<ParticipationRequest> requests;
}
