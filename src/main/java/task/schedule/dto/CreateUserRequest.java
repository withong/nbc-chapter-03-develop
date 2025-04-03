package task.schedule.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

/**
 * 사용자 등록 요청 DTO
 */
@Getter
public class CreateUserRequest {

    /**
     * 이름 (필수, 최대 10자, 문자만 허용)
     */
    @NotBlank(message = "이름을 입력하세요.")
    @Size(max = 10, message = "이름은 10자를 초과할 수 없습니다.")
    @Pattern(regexp = "^[\\p{L}]+$", message = "이름은 문자만 입력 가능합니다.")
    private final String name;

    /**
     * 이메일 (필수, 이메일 형식 검증)
     */
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "올바른 이메일이 아닙니다.")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "올바른 이메일이 아닙니다.")
    private final String email;

    /**
     * 비밀번호 (필수, 최소 8자, 영문자/숫자/특수문자 포함)
     */
    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
            message = "비밀번호는 영문자, 숫자, 특수문자가 포함되어야 합니다."
    )
    private final String password;

    public CreateUserRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
