package task.schedule.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.schedule.common.Const;
import task.schedule.dto.*;
import task.schedule.service.UserService;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 (사용자 등록)
     * @param request 등록할 사용자 정보
     * @return 생성된 사용자 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid CreateUserRequest request) {
        UserResponse response = userService.signUp(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 로그인
     * @param request 로그인 요청 정보
     * @param httpRequest 로그인 세션 처리를 위한 요청 객체
     *                    - 기존 로그인 세션이 존재할 경우 무효화 처리 후 새로운 로그인 세션 생성
     * @return 성공 시 로그인된 사용자 정보
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request,
                                               HttpServletRequest httpRequest) {
        HttpSession existingSession = httpRequest.getSession(false);
        if (existingSession != null) {
            existingSession.invalidate();
        }

        LoginResponse response = userService.login(request.getEmail(), request.getPassword());

        HttpSession session = httpRequest.getSession();
        session.setAttribute(Const.LOGIN_USER, response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 전체 사용자 목록 조회
     * @param pageable 페이징 처리
     * @return 조회된 사용자 목록 + 페이징 정보
     */
    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> findAllUsers(
            @PageableDefault(size = 10, sort = "updatedAt", direction = DESC) Pageable pageable
    ) {
        PageResponse<UserResponse> responses = userService.findAllUsers(pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /**
     * 로그인 후 본인 조회
     * @param request 현재 로그인된 사용자 정보
     * @return 조회된 사용자(본인) 정보
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findMe(HttpServletRequest request) {
        LoginResponse loginUser = (LoginResponse) request.getSession(false).getAttribute(Const.LOGIN_USER);
        UserResponse response = userService.findById(loginUser.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사용자 이름 변경
     * @param request 변경할 이름 정보
     * @param httpRequest 현재 로그인된 사용자 정보
     * @return 변경된 사용자 정보
     */
    @PatchMapping("/info/name")
    public ResponseEntity<UserResponse> updateUserName(HttpServletRequest httpRequest,
                                                       @RequestBody @Valid UpdateNameRequest request) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        UserResponse response = userService.updateName(loginUser.getId(), request.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사용자 비밀번호 변경
     * @param request 변경할 비밀번호 정보
     * @param httpRequest 현재 로그인된 사용자 정보
     * @return 변경된 사용자 정보
     */
    @PatchMapping("/info/password")
    public ResponseEntity<UserResponse> updateUserPassword(HttpServletRequest httpRequest,
                                                           @RequestBody @Valid UpdatePasswordRequest request) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        UserResponse response = userService.updatePassword(
                loginUser.getId(), request.getOldPassword(), request.getNewPassword());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 로그아웃
     * @param httpRequest 현재 로그인된 세션 무효화 처리를 위한 요청 객체
     * @return 성공 시 NO_CONTENT 응답
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest) {
        userService.logout(httpRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 회원 탈퇴 (사용자 삭제)
     * @param httpRequest 현재 로그인된 사용자 정보
     * @return 성공 시 NO_CONTENT 응답
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        userService.deleteUser(loginUser.getId());
        userService.logout(httpRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
