package com.authorization.life.auth.infra.security.password;

import java.util.*;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

/**
 * @author wangjunming
 * @since 2025-06-14 22:02
 */
public class PasswordAuthenticationToken extends AbstractAuthenticationToken {

  public static final String PASSWORD = "password";

  @Getter private final AuthorizationGrantType authorizationGrantType;

  @Getter private final Authentication clientPrincipal;
  @Getter private final String username;
  @Getter private final String password;
  @Getter private final String clientId;
  @Getter private final String clientSecret;

  @Getter private final Set<String> scopes;

  @Getter private final Map<String, Object> additionalParameters;

  public PasswordAuthenticationToken(
      AuthorizationGrantType authorizationGrantType,
      Authentication clientPrincipal,
      String username,
      String password,
      String clientId,
      String clientSecret,
      Set<String> scopes,
      Map<String, Object> additionalParameters) {
    super(Collections.emptyList());
    Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
    Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
    Assert.notNull(username, "username cannot be null");
    Assert.notNull(password, "password cannot be null");
    Assert.notNull(clientId, "clientId cannot be null");
    Assert.notNull(clientSecret, "clientSecret cannot be null");
    this.username = username;
    this.password = password;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.authorizationGrantType = authorizationGrantType;
    this.clientPrincipal = clientPrincipal;
    this.scopes =
        Collections.unmodifiableSet(
            scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
    this.additionalParameters =
        Collections.unmodifiableMap(
            additionalParameters != null
                ? new HashMap<>(additionalParameters)
                : Collections.emptyMap());
  }

  @Override
  public Object getCredentials() {
    return this.password;
  }

  @Override
  public Object getPrincipal() {
    return this.clientPrincipal;
  }
}
