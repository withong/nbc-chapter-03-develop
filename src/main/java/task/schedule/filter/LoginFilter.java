package task.schedule.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;
import task.schedule.common.Const;
import task.schedule.dto.ExceptionResponse;
import task.schedule.exception.ExceptionCode;

import java.io.IOException;

public class LoginFilter implements Filter {

    private static final String[] WHITELIST = {"/users/login", "/users/signup"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        if (!isWhiteList(requestURI)) {
            HttpSession session = httpRequest.getSession(false);

            if (session == null || session.getAttribute(Const.LOGIN_USER) == null) {
                // 사용자 인증 실패 시 응답 반환 처리
                
                // 사용자 정의 예외 코드
                ExceptionCode code = ExceptionCode.NOT_LOGINED;

                // 사용자 정의 예외 응답 객체
                ExceptionResponse responseBody = ExceptionResponse.builder()
                        .status(code.getStatus().value())
                        .code(code.getCode())
                        .message(code.getMessage())
                        .build();

                httpResponse.setStatus(code.getStatus().value());   // 응답 상태 코드 설정
                httpResponse.setContentType("application/json");    // 응답 타입 설정 (JSON)
                httpResponse.setCharacterEncoding("UTF-8");         // 응답 인코딩 설정

                String json = new ObjectMapper().writeValueAsString(responseBody); // 객체를 JSON 문자열로 변환
                httpResponse.getWriter().write(json); // 응답 바디에 JSON 문자열 작성

                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITELIST, requestURI);
    }
}
