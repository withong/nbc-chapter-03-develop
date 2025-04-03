package task.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import task.schedule.entity.Users;

/**
 * 사용자 응답 DTO
 */
@Getter
@AllArgsConstructor
public class UserResponse {

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

    public UserResponse(Users user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
