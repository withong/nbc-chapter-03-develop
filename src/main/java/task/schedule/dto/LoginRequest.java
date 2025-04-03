package task.schedule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 요청 DTO
 */
@Getter
@AllArgsConstructor
public class LoginRequest {

    /**
     * 이메일 (필수, 이메일 형식 검증)
     */
    @Email(message = "올바른 이메일이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private final String email;

    /**
     * 비밀번호 (필수)
     */
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private final String password;
}
