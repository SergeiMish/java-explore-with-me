package ru.practicum.controllers.authenticated;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.dto.comment.CommentDto;
import ru.practicum.service.dto.request.CommentRequestDto;
import ru.practicum.service.interfaces.authenticated.UserCommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
@Validated
public class CommentController {
    private final UserCommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(
            @PathVariable Long eventId,
            @RequestBody @Valid CommentRequestDto commentRequestDto,
            @RequestHeader("X-User-Id") Long userId) {
        return commentService.addComment(userId, eventId, commentRequestDto.getText());
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("X-User-Id") Long userId) {
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping
    public List<CommentDto> getEventComments(@PathVariable Long eventId) {
        return commentService.getEventComments(eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequestDto commentRequestDto,
            @RequestHeader("X-User-Id") Long userId) {
        return commentService.updateComment(userId, commentId, commentRequestDto.getText());
    }
}