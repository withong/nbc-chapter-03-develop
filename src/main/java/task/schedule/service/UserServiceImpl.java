package task.schedule.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.schedule.config.PasswordEncoder;
import task.schedule.dto.LoginResponse;
import task.schedule.dto.PageResponse;
import task.schedule.dto.UserResponse;
import task.schedule.entity.Users;
import task.schedule.exception.CustomException;
import task.schedule.exception.ExceptionCode;
import task.schedule.repository.ScheduleRepository;
import task.schedule.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public UserResponse signUp(String name, String email, String password) {
        boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            throw new CustomException(ExceptionCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(password);

        Users user = new Users(name, email, encodedPassword);
        Users saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

    @Override
    public LoginResponse login(String email, String password) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ExceptionCode.LOGIN_FAILED));

        boolean matched = passwordEncoder.matches(password, user.getPassword());

        if (!matched) {
            throw new CustomException(ExceptionCode.LOGIN_FAILED);
        }

        return new LoginResponse(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public PageResponse<UserResponse> findAllUsers(Pageable pageable) {
        Page<Users> users = userRepository.findAll(pageable);
        Page<UserResponse> page = users.map(UserResponse::new);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
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
    public UserResponse updatePassword(Long id, String oldPassword, String newPassword) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        boolean matched = passwordEncoder.matches(oldPassword, user.getPassword());

        if (!matched) {
            throw new CustomException(ExceptionCode.INVALID_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);

        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public void logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        scheduleRepository.deleteByUser(user);
        userRepository.delete(user);
    }
}
