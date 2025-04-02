package com.authorization.core.security.handle;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 未授权处理器
 *
 * @date 2025-04-02 16:51
 */
@Slf4j
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {

    private String errorPage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        if (response.isCommitted()) {
            log.trace("Did not write to response since already committed");
        } else if (this.errorPage == null) {
            log.debug("Responding with 403 status code");
            response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
        } else {
            request.setAttribute("SPRING_SECURITY_403_EXCEPTION", accessDeniedException);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            log.debug(String.format("Forwarding to %s with status code 403", this.errorPage));

            request.getRequestDispatcher(this.errorPage).forward(request, response);
        }

    }
}
