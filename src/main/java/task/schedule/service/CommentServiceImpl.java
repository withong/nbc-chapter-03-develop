package task.schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.schedule.dto.CommentResponse;
import task.schedule.entity.Comments;
import task.schedule.entity.Schedules;
import task.schedule.entity.Users;
import task.schedule.exception.CustomException;
import task.schedule.exception.ExceptionCode;
import task.schedule.repository.CommentRepository;
import task.schedule.repository.ScheduleRepository;
import task.schedule.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponse saveComment(Long userId, Long scheduleId, String content) {
        Users user = getUserById(userId);
        Schedules schedule = getScheduleById(scheduleId);
        Comments comment = new Comments(user, schedule, content);

        Comments saved = commentRepository.save(comment);

        return new CommentResponse(saved);
    }

    @Override
    public List<CommentResponse> findScheduleComments(Long scheduleId) {
        Schedules schedule = getScheduleById(scheduleId);
        List<Comments> comments = commentRepository.findCommentsBySchedule(schedule);

        return comments.stream().map(CommentResponse::new).toList();
    }

    @Override
    public CommentResponse findCommentById(Long id) {
        Comments comment = getCommentById(id);
        return new CommentResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long userId, Long commentId, String content) {
        Comments comment = getCommentById(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        if (content == null || comment.getContent().equals(content)) {
            throw new CustomException(ExceptionCode.NO_CHANGES);
        }

        comment.updateContent(content);

        return new CommentResponse(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comments comment = getCommentById(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        commentRepository.delete(comment);
    }

    private Comments getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT));
    }

    private Schedules getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE));
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }
}
