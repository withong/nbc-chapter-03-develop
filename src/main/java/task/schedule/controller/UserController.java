package task.schedule.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.schedule.common.Const;
import task.schedule.dto.*;
import task.schedule.service.UserService;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid CreateUserRequest request) {
        UserResponse response = userService.signUp(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request,
                                               HttpServletRequest httpRequest) {
        LoginResponse response = userService.login(request.getEmail(), request.getPassword());

        HttpSession session = httpRequest.getSession();
        session.setAttribute(Const.LOGIN_USER, response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(HttpServletRequest request) {
        LoginResponse loginUser = (LoginResponse) request.getSession(false).getAttribute(Const.LOGIN_USER);
        UserResponse response = userService.findById(loginUser.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/info/name")
    public ResponseEntity<UserResponse> updateUserName(HttpServletRequest httpRequest,
                                                       @RequestBody @Valid UpdateNameRequest request) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        UserResponse response = userService.updateName(loginUser.getId(), request.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/info/password")
    public ResponseEntity<UserResponse> updateUserPassword(HttpServletRequest httpRequest,
                                                           @RequestBody @Valid UpdatePasswordRequest request) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        UserResponse response = userService.updatePassword(
                loginUser.getId(), request.getOldPassword(), request.getNewPassword());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest) {
        userService.logout(httpRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        userService.deleteUser(loginUser.getId());
        userService.logout(httpRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
