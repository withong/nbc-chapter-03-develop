package task.schedule.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.schedule.common.Const;
import task.schedule.dto.CommentRequest;
import task.schedule.dto.CommentResponse;
import task.schedule.dto.LoginResponse;
import task.schedule.dto.PageResponse;
import task.schedule.service.CommentService;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{scheduleId}")
    public ResponseEntity<CommentResponse> saveComment(@NotNull @PathVariable("scheduleId") Long scheduleId,
                                                       @Valid @RequestBody CommentRequest request,
                                                       HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        CommentResponse response = commentService.saveComment(loginUser.getId(), scheduleId, request.getContent());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<PageResponse<CommentResponse>> findScheduleComments(
            @NotNull @PathVariable("scheduleId") Long scheduleId,
            @PageableDefault(size = 10, sort = "updatedAt", direction = DESC) Pageable pageable
    ) {
        PageResponse<CommentResponse> responses = commentService.findScheduleComments(scheduleId, pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> findCommentById(@NotNull @PathVariable("commentId") Long commentId) {
        CommentResponse response = commentService.findCommentById(commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@NotNull @PathVariable("commentId") Long commentId,
                                                         @Valid @RequestBody CommentRequest request,
                                                         HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        CommentResponse response = commentService.updateComment(loginUser.getId(), commentId, request.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@NotNull @PathVariable("commentId") Long commentId,
                                              HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        commentService.deleteComment(loginUser.getId(), commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
