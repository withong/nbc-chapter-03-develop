package task.schedule.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.schedule.dto.CreateUserRequest;
import task.schedule.dto.UpdateNameRequest;
import task.schedule.dto.UpdatePasswordRequest;
import task.schedule.dto.UserResponse;
import task.schedule.service.UserService;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자 등록
     *
     * @param request 등록할 사용자 정보
     *                - [필수] 이름, 이메일, 비밀번호
     * @return 생성된 사용자 정보
     */
    @PostMapping

    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid CreateUserRequest request) {
        UserResponse response = userService.signUp(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 특정 사용자 조회
     *
     * @param id 사용자 식별자
     * @return 조회된 사용자 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@NotNull @PathVariable("id") Long id) {
        UserResponse response = userService.findById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사용자 정보 변경
     *
     * @param id      사용자 식별자
     * @param request 변경할 사용자 정보
     *                - [필수] 이름
     * @return 변경된 사용자 정보
     * - 변경된 내용이 없을 경우 204 No Content 응답
     */
    @PatchMapping("/{id}/name")
    public ResponseEntity<UserResponse> updateUserName(
            @NotNull @PathVariable("id") Long id,
            @RequestBody @Valid UpdateNameRequest request
    ) {
        UserResponse response = userService.updateName(id, request.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<UserResponse> updateUserPassword(
            @NotNull @PathVariable("id") Long id,
            @RequestBody @Valid UpdatePasswordRequest request
    ) {
        return null;
    }

    /**
     * 사용자 삭제
     *
     * @param id 사용자 식별자
     * @return 성공 시 204 No Content 응답
     * - 연관된 일정도 함께 삭제됨
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@NotNull @PathVariable("id") Long id) {
        userService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
