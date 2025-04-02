package task.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.schedule.entity.Comments;
import task.schedule.entity.Schedules;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {

    List<Comments> findCommentsBySchedule(Schedules schedule);
}
