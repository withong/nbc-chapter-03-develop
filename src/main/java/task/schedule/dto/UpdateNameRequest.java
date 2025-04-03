package task.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 사용자 이름 변경 요청 DTO
 */
@Getter
public class UpdateNameRequest {

    /**
     * 변경할 이름 (필수)
     */
    @NotBlank(message = "변경할 이름을 입력하세요.")
    private final String name;

    public UpdateNameRequest(String name) {
        this.name = name;
    }
}
