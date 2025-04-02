package task.schedule.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import task.schedule.dto.ScheduleResponse;
import task.schedule.dto.SearchScheduleRequest;

public interface ScheduleRepositoryCustom {

    Page<ScheduleResponse> findByCondition(Long userId, SearchScheduleRequest condition, Pageable pageable);

    long countSchedulesByCondition(Long userId, SearchScheduleRequest condition);
}
