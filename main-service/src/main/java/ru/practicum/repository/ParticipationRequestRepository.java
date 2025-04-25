package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.enums.RequestStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("SELECT COUNT(pr) FROM ParticipationRequest pr " +
            "WHERE pr.event.id = :eventId AND pr.status = :status")
    Long countByEventIdAndStatus(
            @Param("eventId") Long eventId,
            @Param("status") RequestStatus status);

    List<ParticipationRequest> findByEventId(Long eventId);

    @Query("SELECT pr FROM ParticipationRequest pr " +
            "WHERE pr.event.id = :eventId AND pr.status = ru.practicum.model.enums.RequestStatus.PENDING")
    List<ParticipationRequest> findPendingRequests(@Param("eventId") Long eventId);

    @Query("SELECT pr FROM ParticipationRequest pr WHERE pr.id IN :requestIds")
    List<ParticipationRequest> findAllById(@Param("requestIds") Collection<Long> requestIds);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);
}
