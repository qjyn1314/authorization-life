package com.authorization.life.auth.infra.security.password;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author wangjunming
 * @since 2025-06-14 22:01
 */
@Slf4j
public class PasswordAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    PasswordAuthenticationToken passwordAuthenticationToken =
        (PasswordAuthenticationToken) authentication;

    log.info("PasswordAuthenticationProvider:{}", authentication);
    log.info("PasswordAuthenticationProvider:{}", passwordAuthenticationToken);
    log.info("PasswordAuthenticationProvider:{}", passwordAuthenticationToken);

    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    boolean supports = PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    log.debug("supports authentication=" + authentication + " returning " + supports);
    return supports;
  }
}
