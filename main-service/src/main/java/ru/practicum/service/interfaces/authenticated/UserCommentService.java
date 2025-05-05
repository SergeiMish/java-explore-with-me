package ru.practicum.service.interfaces.authenticated;

import ru.practicum.service.dto.comment.CommentDto;

import java.util.List;

public interface UserCommentService {
    CommentDto addComment(Long userId, Long eventId, String text);

    void deleteComment(Long userId, Long commentId);

    List<CommentDto> getEventComments(Long eventId);

    CommentDto updateComment(Long userId, Long commentId, String text);
}