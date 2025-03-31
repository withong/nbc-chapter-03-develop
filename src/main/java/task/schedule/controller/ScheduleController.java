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

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findSchedulesByUser(
            HttpServletRequest httpRequest
//            @Validated @ModelAttribute SearchScheduleRequest request
    ) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        List<ScheduleResponse> response = scheduleService.findSchedulesByUser(loginUser.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> findScheduleById(@NotNull @PathVariable("id") Long id,
                                                             HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        ScheduleResponse response = scheduleService.findById(id, loginUser.getId());

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

        return null;
    }
}
