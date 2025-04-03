package task.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import task.schedule.entity.Schedules;

import java.time.LocalDate;

/**
 * 일정 응답 DTO
 */
@Getter
@AllArgsConstructor
public class ScheduleResponse {

    /**
     * 일정 식별자
     */
    private final Long id;

    /**
     * 사용자 이름
     */
    private final String userName;

    /**
     * 일정 날짜
     */
    private final LocalDate date;

    /**
     * 일정 제목
     */
    private final String title;

    /**
     * 일정 내용
     */
    private final String content;

    /**
     * 일정별 댓글 개수
     */
    private long commentCount;

    /**
     * 특정 일정의 댓글 목록
     * - 일정 목록 조회 시 null 반환
     * - 일정 단건 조회 시 해당 일정의 댓글 리스트 반환
     * - 페이징 처리 및 페이징 정보 포함
     */
    private PageResponse<CommentResponse> comments;

    /**
     * 일정 등록/수정용 생성자
     * @param schedules 등록/수정 정보가 포함된 일정 객체
     */
    public ScheduleResponse(Schedules schedules) {
        this.id = schedules.getId();
        this.userName = schedules.getUser().getName();
        this.date = schedules.getDate();
        this.title = schedules.getTitle();
        this.content = schedules.getContent();
    }

    /**
     * 일정 목록 조회용 생성자
     * @param schedules 조회된 일정 정보
     * @param commentCount 해당 일정의 댓글 개수
     */
    public ScheduleResponse(Schedules schedules, long commentCount) {
        this.id = schedules.getId();
        this.userName = schedules.getUser().getName();
        this.date = schedules.getDate();
        this.title = schedules.getTitle();
        this.content = schedules.getContent();
        this.commentCount = commentCount;
    }

    /**
     * 일정 단건 조회용 생성자
     * @param schedules 조회된 일정 정보
     * @param commentCount 해당 일정의 댓글 개수
     * @param comments 해당 일정의 댓글 목록 (페이징 처리 및 페이징 정보 포함)
     */
    public ScheduleResponse(Schedules schedules, long commentCount, PageResponse<CommentResponse> comments) {
        this.id = schedules.getId();
        this.userName = schedules.getUser().getName();
        this.date = schedules.getDate();
        this.title = schedules.getTitle();
        this.content = schedules.getContent();
        this.commentCount = commentCount;
        this.comments = comments;
    }
}
