package task.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdatePasswordRequest {

    @NotBlank(message = "현재 비밀번호를 입력하세요.")
    private final String oldPassword;

    @NotBlank(message = "변경할 비밀번호를 입력하세요.")
    private final String newPassword;

    public UpdatePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
