package task.schedule.service;

import org.springframework.data.domain.Pageable;
import task.schedule.dto.CommentResponse;
import task.schedule.dto.PageResponse;

public interface CommentService {

    CommentResponse saveComment(Long userId, Long scheduleId, String content);

    PageResponse<CommentResponse> findScheduleComments(Long scheduleId, Pageable pageable);

    CommentResponse findCommentById(Long id);

    CommentResponse updateComment(Long userId, Long commentId, String content);

    void deleteComment(Long userId, Long commentId);
}
