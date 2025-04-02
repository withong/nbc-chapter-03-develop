package task.schedule.service;

import jakarta.servlet.http.HttpServletRequest;
import task.schedule.dto.LoginResponse;
import task.schedule.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse signUp(String name, String email, String password);

    LoginResponse login(String email, String password);

    List<UserResponse> findAllUsers();

    UserResponse findById(Long id);

    UserResponse updateName(Long id, String name);

    UserResponse updatePassword(Long id, String oldPassword, String newPassword);

    void logout(HttpServletRequest httpRequest);

    void deleteUser(Long id);
}
