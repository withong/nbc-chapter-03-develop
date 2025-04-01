package task.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.schedule.entity.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
}
