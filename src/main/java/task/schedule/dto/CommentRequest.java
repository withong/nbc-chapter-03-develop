package task.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 댓글 등록/수정 요청 DTO
 */
@Getter
@AllArgsConstructor
public class CommentRequest {

    /**
     * 댓글 내용 (필수, 최대 1000자)
     */
    @NotBlank(message = "입력된 내용이 없습니다.")
    @Size(max = 1000, message = "1000자를 초과할 수 없습니다.")
    private final String content;
}
