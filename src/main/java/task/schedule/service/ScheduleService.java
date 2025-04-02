package task.schedule.service;

import task.schedule.dto.ScheduleResponse;
import task.schedule.dto.SearchScheduleRequest;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ScheduleResponse saveSchedule(Long userId, LocalDate date, String content);

    List<ScheduleResponse> findSchedulesByCondition(Long userId, SearchScheduleRequest request);

    ScheduleResponse findUserScheduleWithComments(Long userId, Long id);

    ScheduleResponse updateSchedule(Long id, Long userId, LocalDate date, String content);

    void deleteSchedule(Long id, Long userId);
}

