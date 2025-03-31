package task.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class SearchScheduleRequest {

    private final Integer year;
    private final Integer month;
    private final LocalDate date;
    private final String content;
}
