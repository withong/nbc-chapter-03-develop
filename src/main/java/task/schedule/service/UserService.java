package task.schedule.service;

import task.schedule.dto.UserResponse;

public interface UserService {

    UserResponse signUp(String name, String email, String password);
    UserResponse findById(Long id);
    UserResponse updateName(Long id, String name);
    void deleteUser(Long id);
}
