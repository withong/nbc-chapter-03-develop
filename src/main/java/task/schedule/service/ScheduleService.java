package task.schedule.service;

import org.springframework.data.domain.Pageable;
import task.schedule.dto.PageResponse;
import task.schedule.dto.ScheduleResponse;
import task.schedule.dto.SearchScheduleRequest;

import java.time.LocalDate;

public interface ScheduleService {

    ScheduleResponse saveSchedule(Long userId, LocalDate date, String title, String content);

    PageResponse<ScheduleResponse> findSchedulesByCondition(Long userId, SearchScheduleRequest request, Pageable pageable);

    ScheduleResponse findUserScheduleWithComments(Long userId, Long id);

    ScheduleResponse updateSchedule(Long id, Long userId, LocalDate date, String title, String content);

    void deleteSchedule(Long id, Long userId);
}

