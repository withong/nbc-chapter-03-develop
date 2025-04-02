package task.schedule.service;

import task.schedule.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse saveComment(Long userId, Long scheduleId, String content);

    List<CommentResponse> findScheduleComments(Long scheduleId);

    CommentResponse findCommentById(Long id);

    CommentResponse updateComment(Long userId, Long commentId, String content);

    void deleteComment(Long userId, Long commentId);
}
