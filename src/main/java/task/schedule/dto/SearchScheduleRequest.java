package task.schedule.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class SearchScheduleRequest {

    @Min(value = 1900, message = "1900년 이후부터 조회 가능합니다.")
    @Max(value = 2100, message = "조회 가능한 연도 범위를 초과했습니다.")
    private Integer year;

    @Min(value = 1, message = "유효하지 않은 날짜입니다.")
    @Max(value = 12, message = "유효하지 않은 날짜입니다.")
    private Integer month;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
    private String title;

    @Size(max = 200, message = "내용은 200자를 초과할 수 없습니다.")
    private String content;
}
