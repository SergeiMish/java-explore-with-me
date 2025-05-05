package ru.practicum.service.impl.authenticated;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.model.enums.CommentStatus;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.dto.comment.CommentDto;
import ru.practicum.service.interfaces.authenticated.UserCommentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCommentServiceImpl implements UserCommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    public CommentDto addComment(Long userId, Long eventId, String text) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        Comment comment = Comment.builder()
                .text(text)
                .author(author)
                .event(event)
                .createdOn(LocalDateTime.now())
                .updatedOn(null)
                .build();

        return commentMapper.toDto(commentRepository.save(comment));
    }

    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Only the author can delete the comment");
        }
        comment.setStatus(CommentStatus.DELETED);
        commentRepository.save(comment);
    }

    public List<CommentDto> getEventComments(Long eventId) {
        return commentRepository.findAllByEventId(eventId).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, String text) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Only the author can update the comment");
        }

        if (comment.getStatus() == CommentStatus.DELETED) {
            throw new ConflictException("Cannot update deleted comment");
        }

        comment.setText(text);
        comment.setUpdatedOn(LocalDateTime.now());

        return commentMapper.toDto(commentRepository.save(comment));
    }
}