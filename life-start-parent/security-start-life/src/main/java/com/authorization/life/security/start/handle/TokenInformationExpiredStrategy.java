package com.authorization.life.security.start.handle;

import cn.hutool.json.JSONUtil;
import com.authorization.utils.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 会话信息过期策略，仅允许一个用户对应一个token
 */
@Slf4j
public class TokenInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        log.info("TokenInformationExpiredStrategy--> token expired");
        HttpServletResponse response = event.getResponse();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        out.write(JSONUtil.toJsonStr(Result.fail("life-core.user-login-expired")));
        out.flush();
        out.close();
    }

}
