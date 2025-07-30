package com.authorization.life.auth.infra.security.handler.sso;

import com.alibaba.fastjson2.JSON;
import com.authorization.life.auth.infra.security.sso.ValiVerificationCodeException;
import com.authorization.life.auth.infra.security.sso.VerificationCodeException;
import com.authorization.life.auth.infra.security.vo.AuthCodeExceptionVO;
import com.authorization.utils.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/** 登录失败处理器 */
@Slf4j
public class SsoFailureHandler implements AuthenticationFailureHandler {

  public static final String ERROR_MESSAGE = "Bad credentials";
  public static final String DEFAULT_ERROR_MESSAGE = "用户名或密码错误。";

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    String message = exception.getMessage();
    log.error("Sso登录失败的信息是：", exception);
    Result<AuthCodeExceptionVO> result =
        Result.failForCode(Result.FORM_ERROR, message, handleDataByException(exception));
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    PrintWriter out = response.getWriter();
    out.write(JSON.toJSONString(result));
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
    boolean flag =
        exception instanceof VerificationCodeException
            || exception instanceof ValiVerificationCodeException;
    errorVO.setUsername("").setPassword("").setPhone("").setCaptchaCode("").setDefaultDesc("");
    if (exception instanceof ValiVerificationCodeException) {
      errorVO.setCaptchaCode(message);
    } else if (exception instanceof UsernameNotFoundException
        || exception instanceof LockedException
        || exception instanceof DisabledException
        || exception instanceof AccountExpiredException
        || exception instanceof VerificationCodeException
        || exception instanceof BadCredentialsException) {
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
