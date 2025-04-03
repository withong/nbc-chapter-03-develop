package task.schedule.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import task.schedule.entity.Comments;
import task.schedule.entity.Schedules;

/**
 * 댓글 관련 데이터 접근 인터페이스
 */
public interface CommentRepository extends JpaRepository<Comments, Long> {

    /**
     * 특정 일정의 댓글 목록 조회
     * @param schedule 조회할 일정 정보
     * @param pageable 페이징 정보
     * @return 조회된 댓글 목록 (페이징 처리 및 페이징 정보 포함)
     */
    Page<Comments> findBySchedule(Schedules schedule, Pageable pageable);

    /**
     * 특정 일정의 댓글 개수 조회
     * @param schedule 조회할 일정 정보
     * @return 해당 일정의 댓글 개수
     */
    long countCommentsBySchedule(Schedules schedule);
}
