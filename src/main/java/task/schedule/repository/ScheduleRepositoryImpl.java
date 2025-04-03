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

/**
 * {@code ScheduleRepositoryCustom}의 Querydsl 기반 구현체
 */
@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 조회 조건을 기반으로 특정 사용자의 일정 목록 조회
     * - 일정과 댓글을 LEFT JOIN하여, 각 일정별 댓글 수 함께 조회
     *
     * @param userId 조회할 사용자 식별자
     * @param condition 조회 조건 (년, 월, 날짜, 제목, 내용)
     * @param pageable 페이징 정보
     * @return 조회된 일정 목록 + 일정별 댓글 수 + 페이징 처리 및 페이징 정보
     */
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

    /**
     * 조회 조건에 해당하는 일정 목록의 전체 개수 조회
     * - PageImpl 객체 생성 시 총 데이터 수를 반환하기 위해 사용
     *
     * @param userId 사용자 식별자
     * @param condition 조회 조건
     * @return 조회된 일정 목록의 총 개수
     */
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

    /**
     * 조회 조건을 기반으로 Querydsl의 BooleanBuilder 생성
     * - 조회 조건 중복 제거를 위해 사용
     * - 조회 조건 중 null이 아닌 항목만 동적으로 쿼리에 포함
     *
     * @param userId 사용자 식별자
     * @param condition 조회 조건
     * @return null이 아닌 항목만 포함된 조회 조건
     */
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
