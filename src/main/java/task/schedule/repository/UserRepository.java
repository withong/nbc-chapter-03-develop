package task.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.schedule.entity.Users;

import java.util.Optional;

/**
 * 사용자 관련 데이터 접근 인터페이스
 */
public interface UserRepository extends JpaRepository<Users, Long> {

    /**
     * 이메일 기반 사용자 조회
     * @param email 사용자 이메일
     * @return 조회된 사용자 정보
     */
    Optional<Users> findByEmail(String email);

    /**
     * 이메일 기반 사용자 존재 여부 조회
     * @param email 사용자 이메일
     * @return 사용자 존재 여부
     */
    boolean existsByEmail(String email);
}
