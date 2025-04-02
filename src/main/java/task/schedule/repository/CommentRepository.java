package task.schedule.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import task.schedule.entity.Comments;
import task.schedule.entity.Schedules;

public interface CommentRepository extends JpaRepository<Comments, Long> {

    Page<Comments> findBySchedule(Schedules schedule, Pageable pageable);

    long countCommentsBySchedule(Schedules schedule);
}
