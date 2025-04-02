package task.schedule.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.schedule.common.Const;
import task.schedule.dto.*;
import task.schedule.service.ScheduleService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> saveSchedule(HttpServletRequest httpRequest,
                                                         @RequestBody @Valid CreateScheduleRequest request) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        ScheduleResponse response = scheduleService.saveSchedule(
                loginUser.getId(),
                request.getDate(),
                request.getContent()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ScheduleResponse>> findUserSchedulesByCondition(
            @NotNull @PathVariable("userId") Long userId,
            @Validated @ModelAttribute SearchScheduleRequest request
    ) {
        List<ScheduleResponse> response = scheduleService.findSchedulesByCondition(userId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}/{scheduleId}")
    public ResponseEntity<ScheduleResponse> findUserSchedule(
            @NotNull @PathVariable("userId") Long userId,
            @NotNull @PathVariable("scheduleId") Long scheduleId
    ) {
        ScheduleResponse response = scheduleService.findUserScheduleWithComments(userId, scheduleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule( @NotNull @PathVariable("id") Long id,
                                                            @RequestBody @Valid UpdateScheduleRequest request,
                                                            HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        ScheduleResponse response = scheduleService.updateSchedule(
                id, loginUser.getId(), request.getDate(), request.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@NotNull @PathVariable("id") Long id,
                                               HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        scheduleService.deleteSchedule(id, loginUser.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
