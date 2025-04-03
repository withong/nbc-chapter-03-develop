package task.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 응답 DTO
 */
@Getter
@AllArgsConstructor
public class LoginResponse {

    /**
     * 사용자 식별자
     */
    private final Long id;

    /**
     * 사용자 이름
     */
    private final String name;

    /**
     * 사용자 이메일
     */
    private final String email;
}
