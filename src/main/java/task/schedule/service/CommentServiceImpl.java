package task.schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.schedule.dto.CommentResponse;
import task.schedule.dto.PageResponse;
import task.schedule.entity.Comments;
import task.schedule.entity.Schedules;
import task.schedule.entity.Users;
import task.schedule.exception.CustomException;
import task.schedule.exception.ExceptionCode;
import task.schedule.repository.CommentRepository;
import task.schedule.repository.ScheduleRepository;
import task.schedule.repository.UserRepository;

/**
 * 댓글 관련 내부 로직
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 등록
     * @param userId 현재 로그인된 사용자의 식별자
     * @param scheduleId 댓글을 작성할 일정의 식별자
     * @param content 댓글 내용
     * @return 생성된 댓글 정보
     */
    @Override
    public CommentResponse saveComment(Long userId, Long scheduleId, String content) {
        Users user = getUserById(userId);
        Schedules schedule = getScheduleById(scheduleId);
        Comments comment = new Comments(user, schedule, content);

        Comments saved = commentRepository.save(comment);

        return new CommentResponse(saved);
    }

    /**
     * 특정 일정의 댓글 목록 조회
     * @param scheduleId 조회할 일정의 식별자
     * @param pageable 페이징 정보
     * @return 해당 일정의 댓글 목록 (페이징 처리 및 페이징 정보 포함)
     */
    @Override
    public PageResponse<CommentResponse> findScheduleComments(Long scheduleId, Pageable pageable) {
        Schedules schedule = getScheduleById(scheduleId);
        Page<Comments> comments = commentRepository.findBySchedule(schedule, pageable);
        Page<CommentResponse> page = comments.map(CommentResponse::new);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }

    /**
     * 댓글 단건 조회
     * @param id 댓글 식별자
     * @return 조회된 댓글 정보
     */
    @Override
    public CommentResponse findCommentById(Long id) {
        Comments comment = getCommentById(id);
        return new CommentResponse(comment);
    }

    /**
     * 댓글 수정
     * - 댓글 작성자와 현재 로그인된 사용자가 일치할 경우 수정 가능
     *
     * @param userId 현재 로그인된 사용자의 식별자
     * @param commentId 수정할 댓글의 식별자
     * @param content 수정할 댓글 내용
     * @return 수정된 댓글 정보
     */
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

    /**
     * 댓글 삭제
     * - 댓글 작성자와 현재 로그인된 사용자가 일치할 경우 삭제 가능
     *
     * @param userId 현재 로그인된 사용자의 식별자
     * @param commentId 삭제할 댓글의 식별자
     */
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
