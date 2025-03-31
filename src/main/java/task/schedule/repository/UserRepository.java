package task.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.schedule.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
}
