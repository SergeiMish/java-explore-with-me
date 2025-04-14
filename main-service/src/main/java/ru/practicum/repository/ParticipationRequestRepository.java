package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.enums.ParticipationRequestStatus;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    @Query("SELECT COUNT(pr) FROM ParticipationRequest pr " +
            "WHERE pr.event.id = :eventId AND pr.status = :status")
    Long countByEventIdAndStatus(
            @Param("eventId") Long eventId,
            @Param("status") ParticipationRequestStatus status);
}
