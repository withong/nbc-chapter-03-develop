package task.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import task.schedule.entity.Comments;

import java.time.LocalDateTime;

/**
 * 댓글 응답 DTO
 */
@Getter
@AllArgsConstructor
public class CommentResponse {

    /**
     * 댓글 식별자
     */
    private final Long id;

    /**
     * 일정 식별자
     */
    private final Long scheduleId;

    /**
     * 사용자 이름
     */
    private final String userName;

    /**
     * 댓글 내용
     */
    private final String content;

    /**
     * 마지막 수정 일시
     */
    private final LocalDateTime updatedAt;

    public CommentResponse(Comments comment) {
        this.id = comment.getId();
        this.scheduleId = comment.getSchedule().getId();
        this.userName = comment.getUser().getName();
        this.content = comment.getContent();
        this.updatedAt = comment.getUpdatedAt();
    }
}
