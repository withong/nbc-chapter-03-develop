package task.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import task.schedule.entity.Comments;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private final Long id;
    private final Long scheduleId;
    private final String userName;
    private final String content;
    private final LocalDateTime updatedAt;

    public CommentResponse(Comments comment) {
        this.id = comment.getId();
        this.scheduleId = comment.getSchedule().getId();
        this.userName = comment.getUser().getName();
        this.content = comment.getContent();
        this.updatedAt = comment.getUpdatedAt();
    }
}
