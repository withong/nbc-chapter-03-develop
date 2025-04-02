package task.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import task.schedule.entity.Schedules;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class ScheduleResponse {

    private final Long id;
    private final String userName;
    private final LocalDate date;
    private final String content;
    private List<CommentResponse> comments;

    public ScheduleResponse(Schedules schedules) {
        this.id = schedules.getId();
        this.userName = schedules.getUser().getName();
        this.date = schedules.getDate();
        this.content = schedules.getContent();
    }

    public ScheduleResponse(Schedules schedules, List<CommentResponse> comments) {
        this.id = schedules.getId();
        this.userName = schedules.getUser().getName();
        this.date = schedules.getDate();
        this.content = schedules.getContent();
        this.comments = comments;
    }
}
