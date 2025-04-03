package task.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 사용자 비밀번호 변경 요청 DTO
 */
@Getter
public class UpdatePasswordRequest {

    /**
     * 현재 비밀번호 (필수)
     */
    @NotBlank(message = "현재 비밀번호를 입력하세요.")
    private final String oldPassword;

    /**
     * 변경할 비밀번호 (필수, 최소 8자, 영문자/숫자/특수문자 포함)
     */
    @NotBlank(message = "변경할 비밀번호를 입력하세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
            message = "비밀번호는 영문자, 숫자, 특수문자가 포함되어야 합니다."
    )
    private final String newPassword;

    public UpdatePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
