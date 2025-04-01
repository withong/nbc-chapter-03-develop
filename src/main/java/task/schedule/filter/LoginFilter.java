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
                ExceptionCode code = ExceptionCode.NOT_LOGINED;

                ExceptionResponse responseBody = ExceptionResponse.builder()
                        .status(code.getStatus().value())
                        .code(code.getCode())
                        .message(code.getMessage())
                        .build();

                httpResponse.setStatus(code.getStatus().value());
                httpResponse.setContentType("application/json");
                httpResponse.setCharacterEncoding("UTF-8");

                String json = new ObjectMapper().writeValueAsString(responseBody);
                httpResponse.getWriter().write(json);

                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITELIST, requestURI);
    }
}
