package task.schedule.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import task.schedule.dto.ScheduleResponse;
import task.schedule.dto.SearchScheduleRequest;
import task.schedule.entity.QComments;
import task.schedule.entity.QSchedules;
import task.schedule.entity.QUsers;
import task.schedule.entity.Schedules;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ScheduleResponse> findByCondition(Long userId, SearchScheduleRequest condition, Pageable pageable) {
        QSchedules schedule = QSchedules.schedules;
        QUsers user = QUsers.users;
        QComments comment = QComments.comments;

        BooleanBuilder builder = buildCondition(userId, condition);

        List<Tuple> query = queryFactory
                .select(schedule, comment.count())
                .from(schedule)
                .leftJoin(comment).on(comment.schedule.eq(schedule))
                .join(schedule.user, user)
                .where(builder)
                .groupBy(schedule.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(schedule.updatedAt.desc())
                .fetch();

        List<ScheduleResponse> scheduleResponses = query.stream()
                .map(tuple -> {
                    Schedules schedules = tuple.get(schedule);
                    Long commentCount = tuple.get(comment.count());
                    return new ScheduleResponse(schedules, commentCount);
                })
                .collect(Collectors.toList());

        long count = countSchedulesByCondition(userId, condition);

        return new PageImpl<>(scheduleResponses, pageable, count);
    }

    public long countSchedulesByCondition(Long userId, SearchScheduleRequest condition) {
        QSchedules schedule = QSchedules.schedules;
        QUsers user = QUsers.users;

        BooleanBuilder builder = buildCondition(userId, condition);

        return Optional.ofNullable(
                queryFactory
                        .select(schedule.count())
                        .from(schedule)
                        .join(schedule.user, user)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);
    }

    private BooleanBuilder buildCondition(Long userId, SearchScheduleRequest condition) {
        QSchedules schedule = QSchedules.schedules;
        QUsers user = QUsers.users;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(user.id.eq(userId));
        if (condition.getYear() != null) builder.and(schedule.date.year().eq(condition.getYear()));
        if (condition.getMonth() != null) builder.and(schedule.date.month().eq(condition.getMonth()));
        if (condition.getDate() != null) builder.and(schedule.date.eq(condition.getDate()));
        if (condition.getTitle() != null) builder.and(schedule.title.containsIgnoreCase(condition.getTitle()));
        if (condition.getContent() != null) builder.and(schedule.content.containsIgnoreCase(condition.getContent()));

        return builder;
    }
}
