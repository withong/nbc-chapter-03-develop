package task.schedule.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import task.schedule.dto.ScheduleResponse;
import task.schedule.dto.SearchScheduleRequest;

/**
 * Querydsl 기반의 일정 조회 조건 처리를 위한 커스텀 인터페이스
 * - 실제 구현은 {@code ScheduleRepositoryImpl}에서 담당
 * - {@code ScheduleRepository}에 통합되어 사용
 */
public interface ScheduleRepositoryCustom {

    Page<ScheduleResponse> findByCondition(Long userId, SearchScheduleRequest condition, Pageable pageable);

    long countSchedulesByCondition(Long userId, SearchScheduleRequest condition);
}
