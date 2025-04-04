package task.schedule.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 예외 코드 정의 enum
 */
@Getter
@AllArgsConstructor
public enum ExceptionCode {

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "이미 가입된 이메일입니다."),
    NOT_LOGINED(HttpStatus.UNAUTHORIZED, "NOT_LOGINED", "로그인되지 않은 사용자입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "LOGIN_FAILED", "이메일 또는 비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_ACCESS", "잘못된 접근입니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "입력 값이 유효하지 않습니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_DATE_FORMAT", "올바른 날짜 형식이 아닙니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "NOT_FOUND_USER", "사용자가 존재하지 않습니다."),
    NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "NOT_FOUND_SCHEDULE", "일정이 존재하지 않습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "NOT_FOUND_COMMENT", "댓글이 존재하지 않습니다."),
    UPDATE_FAILED(HttpStatus.NOT_FOUND, "UPDATE_FAILED", "데이터 변경에 실패했습니다."),
    DELETE_FAILED(HttpStatus.NOT_FOUND, "DELETE_FAILED", "데이터 삭제에 실패했습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "INVALID_PASSWORD", "비밀번호가 일치하지 않습니다."),
    NO_CHANGES(HttpStatus.NO_CONTENT, "NO_CHANGES", "변경된 내용이 없습니다."),
    RELOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RELOAD_FAILED", "데이터를 불러오는 데 실패했습니다.");

    /**
     * Http 상태 코드
     */
    private final HttpStatus status;

    /**
     * 사용자 정의 예외 코드
     */
    private final String code;

    /**
     * 예외 메시지
     */
    private final String message;
}
