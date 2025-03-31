package task.schedule.service;

import jakarta.servlet.http.HttpServletRequest;
import task.schedule.dto.LoginResponse;
import task.schedule.dto.UserResponse;

public interface UserService {

    UserResponse signUp(String name, String email, String password);
    LoginResponse login(String email, String password);
    UserResponse findById(Long id);
    UserResponse updateName(Long id, String name);
    UserResponse updatePassword(Long id, String oldPassword, String newPassword);
    void logout(HttpServletRequest httpRequest);
    void deleteUser(Long id);
}
