package task.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 일정 변경 요청 DTO
 */
@Getter
@AllArgsConstructor
public class UpdateScheduleRequest {

    /**
     * 일정 날짜 (선택, yyyy-MM-dd 형식)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    /**
     * 일정 제목 (선택, 최대 50자)
     */
    @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
    private final String title;

    /**
     * 일정 내용 (선택, 최대 200자)
     */
    @Size(max = 200, message = "내용은 200자를 초과할 수 없습니다.")
    private final String content;
}
