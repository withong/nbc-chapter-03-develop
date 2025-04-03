package task.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 일정 등록 요청 DTO
 */
@Getter
public class CreateScheduleRequest {

    /**
     * 일정 날짜 (필수, yyyy-MM-dd 형식)
     */
    @NotNull(message = "날짜는 빈 값일 수 없습니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * 일정 제목 (필수, 최대 50자)
     */
    @NotBlank(message = "제목은 빈 값일 수 없습니다.")
    @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
    private String title;

    /**
     * 일정 내용 (필수, 최대 200자)
     */
    @NotBlank(message = "내용은 빈 값일 수 없습니다.")
    @Size(max = 200, message = "내용은 200자를 초과할 수 없습니다.")
    private String content;
}
