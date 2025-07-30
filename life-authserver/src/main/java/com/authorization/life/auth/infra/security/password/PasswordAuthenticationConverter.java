package com.authorization.life.auth.infra.security.password;

import com.authorization.life.auth.infra.security.OAuth2EndpointUtils;
import com.authorization.utils.json.JsonHelper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * @author wangjunming
 * @since 2025-06-14 22:02
 */
@Slf4j
public class PasswordAuthenticationConverter implements AuthenticationConverter {

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

    // password (REQUIRED)
    String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
    if (!StringUtils.hasText(password)
        || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
      OAuth2EndpointUtils.throwError(
          OAuth2ErrorCodes.INVALID_REQUEST,
          OAuth2ParameterNames.PASSWORD,
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
                        && !e.getKey().equals(OAuth2ParameterNames.CLIENT_ID))
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

    PasswordAuthenticationToken passwordAuthenticationToken =
        new PasswordAuthenticationToken(
            new AuthorizationGrantType(PasswordAuthenticationToken.PASSWORD),
            clientPrincipal,
            username,
            password,
            clientId,
            requestedScopes,
            additionalParameters);
    log.info("passwordAuthenticationToken->{}", JsonHelper.toJson(passwordAuthenticationToken));
    return passwordAuthenticationToken;
  }
}
