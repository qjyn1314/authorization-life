package com.authorization.life.security.handler.sso;

import cn.hutool.json.JSONUtil;
import com.authorization.life.security.sso.ValiVerificationCodeException;
import com.authorization.life.security.sso.VerificationCodeException;
import com.authorization.life.security.vo.AuthCodeExceptionVO;
import com.authorization.start.util.result.Res;
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

    public static final String ERROR_MESSAGE = "Bad credentials";
    public static final String DEFAULT_ERROR_MESSAGE = "用户名或密码错误。";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String message = exception.getMessage();
        log.error("Sso登录失败的信息是：", exception);
        Res<AuthCodeExceptionVO> res = new Res<>(Res.FORM_ERROR, message, handleDataByException(exception));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        out.write(JSONUtil.toJsonStr(res));
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
        if (ERROR_MESSAGE.equals(message)) {
            message = DEFAULT_ERROR_MESSAGE;
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
