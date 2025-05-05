package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(Long eventId);

    List<Comment> findAllByAuthorId(Long authorId);

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long authorId);
}