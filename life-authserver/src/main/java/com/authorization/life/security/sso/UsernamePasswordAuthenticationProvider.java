package com.authorization.life.security.sso;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.authorization.life.entity.User;
import com.authorization.core.security.SecurityConstant;
import com.authorization.start.utils.kvp.KvpFormat;
import com.authorization.life.security.util.RedisCaptchaValidator;
import com.authorization.life.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Optional;
import java.util.Set;

/**
 * 用户名密码登录的校验。
 */
@Slf4j
public class UsernamePasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    public static final String CAPTCHA_UUID = "captchaUuid";
    public static final String CAPTCHA_CODE = "captchaCode";
    public static final String CLIENT_ID = "client_id";

    private static final String PASSWORD_ERROR_COUNT = SecurityConstant.PASSWORD_ERROR_COUNT;

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisHelper;
    private final UserService userService;
    private final RegisteredClientRepository registeredClientService;

    public UsernamePasswordAuthenticationProvider(UserDetailsService userDetailsService,
                                                  PasswordEncoder passwordEncoder,
                                                  RedisTemplate<String, Object> redisHelper,
                                                  UserService userService,
                                                  RegisteredClientRepository registeredClientService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.redisHelper = redisHelper;
        this.userService = userService;
        this.registeredClientService = registeredClientService;
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            Assert.notNull(user, () -> new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation"));
            return user;
        } catch (UsernameNotFoundException notFound) {
            log.debug("User '" + username + "' not found");
            throw notFound;
        } catch (InternalAuthenticationServiceException ex) {
            log.error("在自定义passwordAuthProvider的父类中验证失败，", ex);
            throw ex;
        }
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 检查是否存在验证码
        Object authenticationDetails = authentication.getDetails();
        if (authenticationDetails instanceof CaptchaWebAuthenticationDetails
                && StrUtil.isNotBlank(((CaptchaWebAuthenticationDetails) authenticationDetails).getCaptchaCode())) {
            // 检查验证码正确

            CaptchaWebAuthenticationDetails captcha = (CaptchaWebAuthenticationDetails) authenticationDetails;
            boolean verify = RedisCaptchaValidator.verify(redisHelper, captcha.getCaptchaUuid(), captcha.getCaptchaCode());
            Assert.isTrue(verify, () -> new ValiVerificationCodeException("验证码输入错误。"));
        }
        if (authentication.getCredentials() == null) {
            log.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException("用户名或密码错误。");
        }
        // 检查密码正确
        String presentedPassword = authentication.getCredentials().toString();
        if (passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            // 清除密码错误次数累计
            String cacheKey = KvpFormat.of(PASSWORD_ERROR_COUNT).add("username", userDetails.getUsername()).format();
            redisHelper.delete(cacheKey);
        } else {
            log.debug("Authentication failed: password does not match stored value");
            // 检查密码错误次数
            String cacheKey = KvpFormat.of(PASSWORD_ERROR_COUNT).add("username", userDetails.getUsername()).format();
            int passwordErrorCount = Optional.ofNullable(redisHelper.opsForValue().get(cacheKey)).map(count -> Integer.parseInt(count.toString())).orElse(0);
            if (passwordErrorCount >= 5 && passwordErrorCount < 10) {
                // 未超过10次则密码错误累计次数+1
                redisHelper.opsForValue().set(cacheKey, passwordErrorCount + 1);
                throw new VerificationCodeException("用户名或密码错误。");
            }
            if (passwordErrorCount >= 10) {
                // 输错10次则锁定用户3小时
                userService.lock(((User) userDetails).getUserId(), 3);
            } else {
                // 未超过10次则密码错误累计次数+1
                redisHelper.opsForValue().set(cacheKey, passwordErrorCount + 1);
            }
            throw new BadCredentialsException("用户名或密码错误。");
        }
        // 检查登录端是否与用户组匹配
        if (authenticationDetails instanceof CaptchaWebAuthenticationDetails
                && StrUtil.isNotBlank(((CaptchaWebAuthenticationDetails) authenticationDetails).getClientId())) {
            CaptchaWebAuthenticationDetails clientIdDetails = (CaptchaWebAuthenticationDetails) authenticationDetails;
            String clientId = clientIdDetails.getClientId();
            RegisteredClient registeredClient = registeredClientService.findByClientId(clientId);
            Assert.notNull(registeredClient, () -> new RegClientException("未找到此 OauthClient 信息。"));
            Set<String> allowedScopes = registeredClient.getScopes();
            boolean express = ((User) userDetails).getUserGroups().containsAll(allowedScopes);
            Assert.isTrue(express, () -> new InternalAuthenticationServiceException("用户名或密码错误"));
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
