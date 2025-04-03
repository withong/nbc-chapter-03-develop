package task.schedule.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.schedule.common.Const;
import task.schedule.dto.*;
import task.schedule.service.ScheduleService;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Validated
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 일정 등록
     * @param httpRequest 현재 로그인된 사용자 정보
     * @param request 등록할 일정 정보
     * @return 생성된 일정 정보
     */
    @PostMapping
    public ResponseEntity<ScheduleResponse> saveSchedule(HttpServletRequest httpRequest,
                                                         @RequestBody @Valid CreateScheduleRequest request) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        ScheduleResponse response = scheduleService.saveSchedule(
                loginUser.getId(),
                request.getDate(),
                request.getTitle(),
                request.getContent()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 특정 사용자의 일정 목록 조회
     * @param userId 사용자 식별자
     * @param request 조회 조건
     * @param pageable 페이징 처리
     * @return 일정 목록 + 일정별 댓글 개수 + 페이징 정보
     *         - 일정 목록 조회 시 일정별 댓글 목록(comments)은 null 반환
     */
    @GetMapping("/{userId}")
    public ResponseEntity<PageResponse<ScheduleResponse>> findUserSchedulesByCondition(
            @NotNull @PathVariable("userId") Long userId,
            @Validated @ModelAttribute SearchScheduleRequest request,
            @PageableDefault(size = 10, sort = "updatedAt", direction = DESC) Pageable pageable
    ) {
        PageResponse<ScheduleResponse> response = scheduleService.findSchedulesByCondition(userId, request, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 일정 단건 조회
     * @param userId 사용자 식별자
     * @param scheduleId 일정 식별자
     * @param pageable 페이징 처리
     * @return 조회된 일정 정보 + 해당 일정의 댓글 개수 + 해당 일정의 댓글 목록 + 페이징 정보
     */
    @GetMapping("/{userId}/{scheduleId}")
    public ResponseEntity<ScheduleResponse> findUserSchedule(
            @NotNull @PathVariable("userId") Long userId,
            @NotNull @PathVariable("scheduleId") Long scheduleId,
            @PageableDefault(size = 10, sort = "updatedAt", direction = DESC) Pageable pageable
    ) {
        ScheduleResponse response = scheduleService.findUserScheduleWithComments(userId, scheduleId, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 일정 수정
     * @param id 일정 식별자
     * @param request 수정할 일정 정보
     * @param httpRequest 현재 로그인된 사용자 정보
     * @return 수정된 일정 정보
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule( @NotNull @PathVariable("id") Long id,
                                                            @RequestBody @Valid UpdateScheduleRequest request,
                                                            HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        ScheduleResponse response = scheduleService.updateSchedule(
                id, loginUser.getId(), request.getDate(), request.getTitle(), request.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 일정 삭제
     * @param id 일정 식별자
     * @param httpRequest 현재 로그인된 사용자 정보
     * @return 성공 시 NO_CONTENT 응답
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@NotNull @PathVariable("id") Long id,
                                               HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        scheduleService.deleteSchedule(id, loginUser.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
