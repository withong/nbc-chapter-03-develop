package task.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.schedule.entity.Schedules;
import task.schedule.entity.Users;

import java.util.Optional;

/**
 * 일정 관련 데이터 접근 인터페이스
 */
public interface ScheduleRepository extends JpaRepository<Schedules, Long>, ScheduleRepositoryCustom {

    /**
     * 특정 사용자의 일정 단건 조회
     * @param id 일정 식별자
     * @param user 조회할 사용자 정보
     * @return 조회된 일정 정보
     */
    Optional<Schedules> findByIdAndUser(Long id, Users user);

    /**
     * 특정 사용자의 모든 일정 삭제
     * @param user 사용자 정보
     */
    void deleteByUser(Users user);
}
