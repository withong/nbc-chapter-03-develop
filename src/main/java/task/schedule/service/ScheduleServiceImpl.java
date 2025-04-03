package task.schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.schedule.dto.CommentResponse;
import task.schedule.dto.PageResponse;
import task.schedule.dto.ScheduleResponse;
import task.schedule.dto.SearchScheduleRequest;
import task.schedule.entity.Comments;
import task.schedule.entity.Schedules;
import task.schedule.entity.Users;
import task.schedule.exception.CustomException;
import task.schedule.exception.ExceptionCode;
import task.schedule.repository.CommentRepository;
import task.schedule.repository.ScheduleRepository;
import task.schedule.repository.UserRepository;

import java.time.LocalDate;

/**
 * 일정 관련 내부 로직
 */
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /**
     * 일정 등록
     * @param userId 현재 로그인된 사용자의 식별자
     * @param date 일정 날짜
     * @param title 일정 제목
     * @param content 일정 내용
     * @return 등록된 일정 정보
     */
    @Override
    public ScheduleResponse saveSchedule(Long userId, LocalDate date, String title, String content) {
        Users user = getUserById(userId);
        Schedules schedule = new Schedules(user, date, title, content);
        Schedules saved = scheduleRepository.save(schedule);

        return new ScheduleResponse(saved);
    }

    /**
     * 조회 조건 기반 특정 사용자의 일정 목록 조회
     * @param userId 조회할 사용자의 식별자
     * @param request 조회 조건
     * @param pageable 페이징 정보
     * @return 해당 사용자의 일정 목록 (댓글 수, 페이징 처리, 페이징 정보 포함)
     */
    @Override
    public PageResponse<ScheduleResponse> findSchedulesByCondition(Long userId, SearchScheduleRequest request, Pageable pageable) {
        Users user = getUserById(userId);
        Page<ScheduleResponse> page = scheduleRepository.findByCondition(user.getId(), request, pageable);

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
     * 특정 사용자의 일정 단건 조회 (해당 일정의 댓글 목록 포함)
     * @param userId 조회할 사용자의 식별자
     * @param id 조회할 일정의 식별자
     * @param pageable 페이징 정보
     * @return 조회된 일정 정보 (해당 일정의 댓글 수, 댓글 목록, 댓글 페이징 처리, 댓글 페이징 정보 포함)
     */
    @Override
    public ScheduleResponse findUserScheduleWithComments(Long userId, Long id, Pageable pageable) {
        Users user = getUserById(userId);
        Schedules schedule = scheduleRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE));

        long commentCount = commentRepository.countCommentsBySchedule(schedule);

        Page<Comments> comments = commentRepository.findBySchedule(schedule, pageable);
        Page<CommentResponse> page = comments.map(CommentResponse::new);
        PageResponse<CommentResponse> commentResponses = new PageResponse<>(
                page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getTotalPages(), page.isFirst(), page.isLast(), page.isEmpty()
        );

        return new ScheduleResponse(schedule, commentCount, commentResponses);
    }

    /**
     * 일정 수정
     * - 일정 작성자와 현재 로그인된 사용자가 일치할 경우 수정 가능
     * - 변경할 내용이 없을 경우 NO_CHANGES 응답
     *
     * @param id 수정할 일정의 식별자
     * @param userId 현재 로그인된 사용자의 식별자
     * @param date 수정할 일정 날짜
     * @param title 수정할 일정 제목
     * @param content 수정할 일정 내용
     * @return 수정된 일정 정보
     */
    @Override
    @Transactional
    public ScheduleResponse updateSchedule(Long id, Long userId, LocalDate date, String title, String content) {
        if (date == null && title == null && content == null) {
            throw new CustomException(ExceptionCode.NO_CHANGES);
        }

        Schedules schedule = getScheduleById(id);

        if (!schedule.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        if (schedule.getDate().equals(date)
                && schedule.getTitle().equals(title)
                && schedule.getContent().equals(content)) {
            throw new CustomException(ExceptionCode.NO_CHANGES);
        }

        if (date != null) schedule.updateDate(date);
        if (title != null) schedule.updateTitle(title);
        if (content != null) schedule.updateContent(content);

        return new ScheduleResponse(schedule);
    }

    /**
     * 일정 삭제
     * - 일정 작성자와 현재 로그인된 사용자가 일치할 경우 삭제 가능
     *
     * @param id 삭제할 일정 식별자
     * @param userId 현재 로그인된 사용자의 식별자
     */
    @Override
    @Transactional
    public void deleteSchedule(Long id, Long userId) {
        Schedules schedule = getScheduleById(id);

        if (!schedule.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        scheduleRepository.delete(schedule);
    }

    private Schedules getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE));
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }
}
