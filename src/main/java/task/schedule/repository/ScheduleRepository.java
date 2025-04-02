package task.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.schedule.entity.Schedules;
import task.schedule.entity.Users;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedules, Long>, ScheduleRepositoryCustom {

    Optional<Schedules> findByIdAndUser(Long id, Users user);

    void deleteByUser(Users user);
}
