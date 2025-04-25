package com.authorization.life.auth.infra.security.handler.sso;

import cn.hutool.json.JSONUtil;
import com.authorization.utils.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 单点登录成功处理类
 */
@Slf4j
public class SsoSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("密码验证登录成功的对象信息是-{}", JSONUtil.toJsonStr(authentication));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        try {
            PrintWriter out = response.getWriter();
            out.write(JSONUtil.toJsonStr(Result.ok(authentication)));
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("登录成功处理器处理失败，返回数据异常.", e);
        }
    }
}
