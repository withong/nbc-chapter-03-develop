package task.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.schedule.entity.Schedules;

public interface ScheduleRepository extends JpaRepository<Schedules, Long>, ScheduleRepositoryCustom {

}
