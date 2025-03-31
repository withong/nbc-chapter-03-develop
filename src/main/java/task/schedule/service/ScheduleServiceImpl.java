package task.schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.schedule.dto.ScheduleResponse;
import task.schedule.entity.Schedules;
import task.schedule.entity.Users;
import task.schedule.exception.CustomException;
import task.schedule.exception.ExceptionCode;
import task.schedule.repository.ScheduleRepository;
import task.schedule.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    public ScheduleResponse saveSchedule(Long userId, LocalDate date, String content) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        Schedules schedule = new Schedules(user, date, content);
        Schedules saved = scheduleRepository.save(schedule);

        return new ScheduleResponse(saved);
    }

    @Override
    public List<ScheduleResponse> findSchedulesByUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        List<Schedules> list = scheduleRepository.findByUser(user);

        return list.stream().map(ScheduleResponse::new).toList();
    }

    @Override
    public ScheduleResponse findById(Long id, Long userId) {
        Schedules schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE));

        if (!schedule.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        return new ScheduleResponse(schedule);
    }

    @Override
    @Transactional
    public ScheduleResponse updateSchedule(Long id, Long userId, LocalDate date, String content) {
        if (date == null && content == null) {
            throw new CustomException(ExceptionCode.NO_CHANGES);
        }

        Schedules schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE));

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
        Schedules schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE));

        if (!schedule.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        scheduleRepository.delete(schedule);
    }
}
