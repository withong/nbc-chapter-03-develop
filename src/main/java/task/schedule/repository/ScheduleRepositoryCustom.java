package task.schedule.repository;

import task.schedule.dto.SearchScheduleRequest;
import task.schedule.entity.Schedules;

import java.util.List;

public interface ScheduleRepositoryCustom {

    List<Schedules> findByCondition(Long userId, SearchScheduleRequest condition);
}
