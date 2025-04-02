package task.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import task.schedule.entity.Users;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;

    public UserResponse(Users user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
