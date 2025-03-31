package task.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateNameRequest {

    @NotBlank(message = "변경할 이름을 입력하세요.")
    private final String name;

    public UpdateNameRequest(String name) {
        this.name = name;
    }
}
