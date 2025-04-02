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

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public ScheduleResponse saveSchedule(Long userId, LocalDate date, String title, String content) {
        Users user = getUserById(userId);
        Schedules schedule = new Schedules(user, date, title, content);
        Schedules saved = scheduleRepository.save(schedule);

        return new ScheduleResponse(saved);
    }

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
