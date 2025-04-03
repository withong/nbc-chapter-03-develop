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

    /**
     * 댓글 등록
     * @param scheduleId 일정 식별자
     * @param request 등록할 댓글 정보
     * @param httpRequest 현재 로그인된 사용자 정보
     * @return 생성된 댓글 정보
     */
    @PostMapping("/{scheduleId}")
    public ResponseEntity<CommentResponse> saveComment(@NotNull @PathVariable("scheduleId") Long scheduleId,
                                                       @Valid @RequestBody CommentRequest request,
                                                       HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        CommentResponse response = commentService.saveComment(loginUser.getId(), scheduleId, request.getContent());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 특정 일정의 댓글 목록 조회
     * @param scheduleId 일정 식별자
     * @param pageable 페이징 처리
     * @return 조회된 일정 정보 + 해당 일정의 댓글 목록 + 페이징 정보
     */
    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<PageResponse<CommentResponse>> findScheduleComments(
            @NotNull @PathVariable("scheduleId") Long scheduleId,
            @PageableDefault(size = 10, sort = "updatedAt", direction = DESC) Pageable pageable
    ) {
        PageResponse<CommentResponse> responses = commentService.findScheduleComments(scheduleId, pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /**
     * 댓글 단건 조회
     * @param commentId 댓글 식별자
     * @return 조회된 댓글 정보
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> findCommentById(@NotNull @PathVariable("commentId") Long commentId) {
        CommentResponse response = commentService.findCommentById(commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 댓글 수정
     * @param commentId 댓글 식별자
     * @param request 수정할 댓글 정보
     * @param httpRequest 현재 로그인된 사용자 정보
     * @return 수정된 댓글 정보
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@NotNull @PathVariable("commentId") Long commentId,
                                                         @Valid @RequestBody CommentRequest request,
                                                         HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        CommentResponse response = commentService.updateComment(loginUser.getId(), commentId, request.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 댓글 삭제
     * @param commentId 댓글 식별자
     * @param httpRequest 현재 로그인된 사용자 정보
     * @return 성공 시 NO_CONTENT 응답
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@NotNull @PathVariable("commentId") Long commentId,
                                              HttpServletRequest httpRequest) {
        LoginResponse loginUser = (LoginResponse) httpRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        commentService.deleteComment(loginUser.getId(), commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
