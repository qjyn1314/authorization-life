package com.authorization.core.security.handle;

import cn.hutool.json.JSONUtil;
import com.authorization.start.util.result.Res;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 会话信息过期策略，仅允许一个用户对应一个token
 */
public class TokenInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    public static final String EXPIRED_STRATEGY_MSG = "当前登录过期，请重新登录。";

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletResponse response = event.getResponse();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        out.write(JSONUtil.toJsonStr(Res.fail(EXPIRED_STRATEGY_MSG, null)));
        out.flush();
        out.close();
    }


}
