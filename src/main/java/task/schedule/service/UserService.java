package task.schedule.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import task.schedule.dto.LoginResponse;
import task.schedule.dto.PageResponse;
import task.schedule.dto.UserResponse;

public interface UserService {

    UserResponse signUp(String name, String email, String password);

    LoginResponse login(String email, String password);

    PageResponse<UserResponse> findAllUsers(Pageable pageable);

    UserResponse findById(Long id);

    UserResponse updateName(Long id, String name);

    UserResponse updatePassword(Long id, String oldPassword, String newPassword);

    void logout(HttpServletRequest httpRequest);

    void deleteUser(Long id);
}
