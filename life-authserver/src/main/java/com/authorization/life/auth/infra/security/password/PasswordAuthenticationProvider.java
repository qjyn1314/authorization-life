package com.authorization.life.auth.infra.security.password;

import cn.hutool.extra.spring.SpringUtil;
import com.authorization.life.auth.infra.security.base.OAuth2BaseAuthenticationProvider;
import com.authorization.life.security.start.UserDetailService;
import com.authorization.redis.start.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * @author wangjunming
 * @since 2025-06-14 22:01
 */
@Slf4j
public class PasswordAuthenticationProvider
    extends OAuth2BaseAuthenticationProvider<PasswordAuthenticationToken> {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    PasswordAuthenticationToken passwordAuthenticationToken =
        (PasswordAuthenticationToken) authentication;
    UserDetailService userDetailService = SpringUtil.getBean(UserDetailService.class);
    PasswordEncoder passwordEncoder = SpringUtil.getBean(PasswordEncoder.class);
    RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
    RegisteredClientRepository registeredClientRepository =
        SpringUtil.getBean(RegisteredClientRepository.class);



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
