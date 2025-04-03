package task.schedule.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 일정 목록 조회 요청 DTO
 */
@Setter
@Getter
@AllArgsConstructor
public class SearchScheduleRequest {

    /**
     * 일정이 작성된 연도 (선택, 1900 ~ 2100 범위만 허용)
     */
    @Min(value = 1900, message = "1900년 이후부터 조회 가능합니다.")
    @Max(value = 2100, message = "조회 가능한 연도 범위를 초과했습니다.")
    private Integer year;

    /**
     * 일정이 작성된 월 (선택, 1 ~ 12 범위만 허용)
     */
    @Min(value = 1, message = "유효하지 않은 날짜입니다.")
    @Max(value = 12, message = "유효하지 않은 날짜입니다.")
    private Integer month;

    /**
     * 일정이 작성된 날짜 (선택, yyyy-MM-dd 형식)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * 일정 제목 (선택, 최대 50자)
     */
    @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
    private String title;

    /**
     * 일정 내용 (선택, 최대 200자)
     */
    @Size(max = 200, message = "내용은 200자를 초과할 수 없습니다.")
    private String content;
}
