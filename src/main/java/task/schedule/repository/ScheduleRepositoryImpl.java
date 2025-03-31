package task.schedule.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import task.schedule.dto.SearchScheduleRequest;
import task.schedule.entity.QSchedules;
import task.schedule.entity.QUsers;
import task.schedule.entity.Schedules;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Schedules> findByCondition(Long userId, SearchScheduleRequest condition) {
        QSchedules schedule = QSchedules.schedules;
        QUsers user = QUsers.users;

        return queryFactory
                .selectFrom(schedule)
                .join(schedule.user, user)
                .where(
                        user.id.eq(userId),
                        condition.getYear() != null ? schedule.date.year().eq(condition.getYear()) : null,
                        condition.getMonth() != null ? schedule.date.month().eq(condition.getMonth()) : null,
                        condition.getDate() != null ? schedule.date.eq(condition.getDate()) : null,
                        condition.getContent() != null ? schedule.content.containsIgnoreCase(condition.getContent()) : null
                )
                .fetch();
    }
}
