package task.schedule.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 정의 예외 클래스 - 예외 발생 시 ExceptionCode를 통해 예외 정보 전달
 */
@Getter
public class CustomException extends RuntimeException {
    /**
     * 예외 코드 (ExceptionCode enum)
     */
    private final ExceptionCode exceptionCode;

    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
