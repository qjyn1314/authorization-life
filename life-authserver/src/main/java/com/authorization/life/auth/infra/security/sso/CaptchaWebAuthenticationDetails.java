package com.authorization.life.auth.infra.security.sso;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Objects;

/**
 * 用户名密码登录-验证码
 */
public class CaptchaWebAuthenticationDetails  extends WebAuthenticationDetails {

    private final String captchaCode;
    private final String captchaUuid;
    private final String clientId;

    public CaptchaWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        captchaCode = request.getParameter(UsernamePasswordAuthenticationProvider.CAPTCHA_CODE);
        captchaUuid = request.getParameter(UsernamePasswordAuthenticationProvider.CAPTCHA_UUID);
        clientId = request.getParameter(UsernamePasswordAuthenticationProvider.CLIENT_ID);
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public String getCaptchaUuid() {
        return captchaUuid;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }

        CaptchaWebAuthenticationDetails that = (CaptchaWebAuthenticationDetails) object;

        return Objects.equals(captchaCode, that.captchaCode);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (captchaCode != null ? captchaCode.hashCode() : 0);
        return result;
    }
}
