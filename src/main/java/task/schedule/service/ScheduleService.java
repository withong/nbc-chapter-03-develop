package task.schedule.service;

import task.schedule.dto.ScheduleResponse;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ScheduleResponse saveSchedule(Long userId, LocalDate date, String content);
    List<ScheduleResponse> findSchedulesByUser(Long userId);
    ScheduleResponse findById(Long id, Long userId);
    ScheduleResponse updateSchedule(Long id, Long userId, LocalDate date, String content);
    void deleteSchedule(Long id, Long userId);
}

