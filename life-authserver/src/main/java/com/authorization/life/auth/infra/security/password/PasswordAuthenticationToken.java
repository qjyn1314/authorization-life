package com.authorization.life.auth.infra.security.password;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * @author wangjunming
 * @since 2025-06-14 22:02
 */
public class PasswordAuthenticationToken extends AbstractAuthenticationToken {

  public static final String PASSWORD = "password";

  @Getter private final AuthorizationGrantType authorizationGrantType;

  @Getter private final Authentication clientPrincipal;

  @Getter private final Set<String> scopes;

  @Getter private final Map<String, Object> additionalParameters;

  public PasswordAuthenticationToken(
      AuthorizationGrantType authorizationGrantType,
      Authentication clientPrincipal,
      Set<String> scopes,
      Map<String, Object> additionalParameters) {
    super(Collections.emptyList());
    this.authorizationGrantType = authorizationGrantType;
    this.clientPrincipal = clientPrincipal;
    this.scopes = scopes;
    this.additionalParameters = additionalParameters;
  }

  @Override
  public Object getCredentials() {
    return "";
  }

  @Override
  public Object getPrincipal() {
    return this.clientPrincipal;
  }
}
