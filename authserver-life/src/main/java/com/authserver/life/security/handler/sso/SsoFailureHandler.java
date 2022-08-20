package com.authserver.life.security.handler.sso;

import cn.hutool.json.JSONUtil;
import com.authserver.common.result.Result;
import com.authserver.life.security.sso.ValiVerificationCodeException;
import com.authserver.life.security.sso.VerificationCodeException;
import com.authserver.life.security.vo.AuthCodeExceptionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 登录失败处理器
 * </p>
 */
@Slf4j
public class SsoFailureHandler implements AuthenticationFailureHandler {

    public static final String error_message = "Bad credentials";
    public static final String default_error_message = "用户名或密码错误。";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String message = exception.getMessage();
        log.error("Sso登录失败的信息是：", exception);
        Result<AuthCodeExceptionVO> result = new Result<>(Result.FORM_ERROR, message, handleDataByException(exception));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        out.write(JSONUtil.toJsonStr(result));
        out.flush();
        out.close();
    }


    /**
     * SSO登录错误异常返回数据处理
     *
     * @param exception 登录异常
     * @return AuthCodeExceptionVO 异常信息封装
     */
    private AuthCodeExceptionVO handleDataByException(AuthenticationException exception) {
        String message = exception.getMessage();
        if (error_message.equals(message)) {
            message = default_error_message;
        }
        AuthCodeExceptionVO authCodeExceptionVO = new AuthCodeExceptionVO();
        AuthCodeExceptionVO.ErrorVO errorVO = new AuthCodeExceptionVO.ErrorVO();
        boolean flag = false;
        if (exception instanceof VerificationCodeException) {
            flag = true;
        }
        errorVO.setUsername("").setPassword("").setPhone("").setCaptchaCode("").setDefaultDesc("");
        if (exception instanceof ValiVerificationCodeException) {
            errorVO.setCaptchaCode(message);
        } else if (exception instanceof UsernameNotFoundException || exception instanceof LockedException ||
                exception instanceof DisabledException || exception instanceof AccountExpiredException ||
                exception instanceof VerificationCodeException || exception instanceof BadCredentialsException) {
            errorVO.setUsername(message);
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorVO.setPassword(message);
        } else {
            errorVO.setDefaultDesc(message);
        }
        authCodeExceptionVO.setErrorVO(errorVO).setShowCaptchaCode(flag);
        return authCodeExceptionVO;
    }

}
