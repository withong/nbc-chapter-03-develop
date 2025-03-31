package task.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UpdateScheduleRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    @Size(max = 200, message = "200자를 초과할 수 없습니다.")
    private final String content;
}
