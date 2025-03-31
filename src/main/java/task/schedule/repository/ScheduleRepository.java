package task.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.schedule.entity.Schedules;
import task.schedule.entity.Users;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedules, Long> {

    List<Schedules> findByUser(Users user);
}
