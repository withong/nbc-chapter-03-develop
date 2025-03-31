package task.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateScheduleRequest {

    @NotNull(message = "날짜는 빈 값일 수 없습니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotBlank(message = "내용은 빈 값일 수 없습니다.")
    @Size(max = 200, message = "200자를 초과할 수 없습니다.")
    private String content;
}
