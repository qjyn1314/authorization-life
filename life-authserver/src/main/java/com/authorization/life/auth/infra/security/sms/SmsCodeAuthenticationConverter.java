package com.authorization.life.auth.infra.security.sms;

import com.authorization.life.auth.infra.security.OAuth2EndpointUtils;
import com.authorization.life.auth.infra.security.password.PasswordAuthenticationToken;
import com.authorization.utils.json.JsonHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangjunming
 * @since 2025-06-14 22:02
 */
@Slf4j
public class SmsCodeAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {

        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getFormParameters(request);

        // grant_type (REQUIRED)
        String grantType = parameters.getFirst(OAuth2ParameterNames.GRANT_TYPE);
        if (!PasswordAuthenticationToken.PASSWORD.equals(grantType)) {
            return null;
        }

        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username)
                || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.USERNAME,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // captchaUuid (REQUIRED)
        String captchaUuid = parameters.getFirst(SmsCodeAuthenticationProvider.CAPTCHA_UUID);
        if (!StringUtils.hasText(captchaUuid)
                || parameters.get(SmsCodeAuthenticationProvider.CAPTCHA_UUID).size() != 1) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    SmsCodeAuthenticationProvider.CAPTCHA_UUID,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // captchaCode (REQUIRED)
        String captchaCode = parameters.getFirst(SmsCodeAuthenticationProvider.CAPTCHA_CODE);
        if (!StringUtils.hasText(captchaCode)
                || parameters.get(SmsCodeAuthenticationProvider.CAPTCHA_CODE).size() != 1) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    SmsCodeAuthenticationProvider.CAPTCHA_CODE,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // clientId (REQUIRED)
        String clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID);
        if (!StringUtils.hasText(clientId)
                || parameters.get(OAuth2ParameterNames.CLIENT_ID).size() != 1) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.CLIENT_ID,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // clientSecret (REQUIRED)
        String clientSecret = parameters.getFirst(OAuth2ParameterNames.CLIENT_SECRET);
        if (!StringUtils.hasText(clientSecret)
                || parameters.get(OAuth2ParameterNames.CLIENT_SECRET).size() != 1) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.CLIENT_SECRET,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // scope (OPTIONAL)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.SCOPE,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes =
                    new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, ",")));
        }

        // 获取当前已经认证的客户端信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (clientPrincipal == null) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ErrorCodes.INVALID_CLIENT,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // 扩展信息
        Map<String, Object> additionalParameters =
                parameters.entrySet().stream()
                        .filter(
                                e ->
                                        !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE)
                                                && !e.getKey().equals(OAuth2ParameterNames.USERNAME)
                                                && !e.getKey().equals(OAuth2ParameterNames.PASSWORD)
                                                && !e.getKey().equals(OAuth2ParameterNames.SCOPE)
                                                && !e.getKey().equals(OAuth2ParameterNames.CLIENT_ID)
                                                && !e.getKey().equals(OAuth2ParameterNames.CLIENT_SECRET))
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

        SmsCodeAuthenticationToken smsCodeAuthenticationToken = new SmsCodeAuthenticationToken(
                new AuthorizationGrantType(SmsCodeAuthenticationToken.PHONE_CAPTCHA),
                clientPrincipal,
                username,
                captchaUuid,
                captchaCode,
                clientId,
                clientSecret,
                requestedScopes,
                additionalParameters
        );

        log.info("passwordAuthenticationToken->{}", JsonHelper.toJson(smsCodeAuthenticationToken));
        return smsCodeAuthenticationToken;
    }
}
