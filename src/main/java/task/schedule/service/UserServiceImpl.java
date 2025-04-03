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

/**
 * 사용자 관련 내부 로직
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * 사용자 등록
     * @param name 등록할 이름
     * @param email 등록할 이메일
     * @param password 등록할 비밀번호
     * @return 생성된 사용자 정보
     */
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

    /**
     * 로그인
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 성공 시 로그인된 사용자 정보
     *         - 실패 시 LOGIN_FAILED 응답
     */
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

    /**
     * 전체 사용자 조회
     * @param pageable 페이징 정보
     * @return 조회된 사용자 목록 (페이징 처리, 페이징 정보 포함)
     */
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

    /**
     * 특정 사용자 조회
     * @param id 조회할 사용자의 식별자
     * @return 조회된 사용자 정보
     */
    @Override
    public UserResponse findById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    /**
     * 사용자 이름 변경
     * @param id 현재 로그인된 사용자의 식별자
     * @param name 변경할 이름
     * @return 변경된 사용자 정보
     */
    @Override
    @Transactional
    public UserResponse updateName(Long id, String name) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        user.updateName(name);

        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    /**
     * 사용자 비밀번호 변경
     * - 변경 전 현재 비밀번호 검증 진행
     *
     * @param id 현재 로그인된 사용자의 식별자
     * @param oldPassword 현재 비밀번호
     * @param newPassword 변경할 비밀번호
     * @return 성공 시 사용자 정보 (비밀번호 미포함)
     */
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

    /**
     * 로그아웃
     * @param httpRequest 현재 로그인된 사용자의 세션 정보
     */
    @Override
    public void logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * 사용자 삭제
     * @param id 현재 로그인된 사용자의 식별자
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        scheduleRepository.deleteByUser(user);
        userRepository.delete(user);
    }
}
