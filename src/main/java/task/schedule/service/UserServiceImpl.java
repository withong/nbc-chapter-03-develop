package task.schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.schedule.dto.UserResponse;
import task.schedule.entity.Users;
import task.schedule.exception.CustomException;
import task.schedule.exception.ExceptionCode;
import task.schedule.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse signUp(String name, String email, String password) {

        Users user = new Users(name, email, password);
        Users saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

    @Override
    public UserResponse findById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    @Transactional
    public UserResponse updateName(Long id, String name) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        user.updateName(name);

        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        userRepository.delete(user);
    }
}
