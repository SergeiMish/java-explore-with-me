package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.enums.EventState;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    boolean existsByCategoryId(Long categoryId);

    List<Event> findByInitiatorId(Long userId);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    boolean existsByIdAndInitiatorId(Long eventId, Long userId);

    Optional<Event> findByIdAndState(Long id, EventState state);

    @Modifying
    @Query("UPDATE Event e SET e.views = e.views + 1 WHERE e.id = :id")
    void incrementViews(@Param("id") Long id);

    List<Event> findByState(EventState state);
}
