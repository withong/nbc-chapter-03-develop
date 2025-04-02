package task.schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.schedule.dto.CommentResponse;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public ScheduleResponse saveSchedule(Long userId, LocalDate date, String content) {
        Users user = getUserById(userId);
        Schedules schedule = new Schedules(user, date, content);
        Schedules saved = scheduleRepository.save(schedule);

        return new ScheduleResponse(saved);
    }

    @Override
    public List<ScheduleResponse> findSchedulesByCondition(Long userId, SearchScheduleRequest request) {
        Users user = getUserById(userId);
        List<Schedules> list = scheduleRepository.findByCondition(user.getId(), request);

        return list.stream().map(ScheduleResponse::new).toList();
    }

    @Override
    public ScheduleResponse findUserScheduleWithComments(Long userId, Long id) {
        Users user = getUserById(userId);
        Schedules schedule = scheduleRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE));

        List<Comments> comments = commentRepository.findCommentsBySchedule(schedule);
        List<CommentResponse> commentResponses = comments.stream().map(CommentResponse::new).toList();

        return new ScheduleResponse(schedule, commentResponses);
    }

    @Override
    @Transactional
    public ScheduleResponse updateSchedule(Long id, Long userId, LocalDate date, String content) {
        if (date == null && content == null) {
            throw new CustomException(ExceptionCode.NO_CHANGES);
        }

        Schedules schedule = getScheduleById(id);

        if (!schedule.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        if (schedule.getDate().equals(date) && schedule.getContent().equals(content)) {
            throw new CustomException(ExceptionCode.NO_CHANGES);
        }

        if (date != null) schedule.updateDate(date);
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
