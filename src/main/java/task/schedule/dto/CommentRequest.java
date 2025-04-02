package task.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequest {

    @NotBlank(message = "입력된 내용이 없습니다.")
    @Size(max = 1000, message = "1000자를 초과할 수 없습니다.")
    private final String content;
}
