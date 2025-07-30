package com.authorization.life.auth.infra.security.base;

import java.util.Map;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * @author wangjunming
 * @since 2025-07-30 16:31
 */
public abstract class OAuth2BaseAuthenticationProvider<T extends AbstractAuthenticationToken>
    implements AuthenticationProvider {

  public OAuth2AccessTokenAuthenticationToken createOAuth2AccessTokenAuthenticationToken(
      RegisteredClient registeredClient,
      Authentication clientPrincipal,
      Map<String, Object> additionalParameters) {
    T clientPrincipalToken = (T)clientPrincipal;
    OAuth2RefreshToken refreshToken = null;
    OAuth2AccessToken accessToken = null;

    return new OAuth2AccessTokenAuthenticationToken(
        registeredClient, clientPrincipalToken, accessToken, refreshToken, additionalParameters);
  }
}
