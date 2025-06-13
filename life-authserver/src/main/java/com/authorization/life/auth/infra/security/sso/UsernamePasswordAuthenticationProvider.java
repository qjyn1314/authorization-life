package com.authorization.life.auth.infra.security.sso;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.authorization.life.auth.app.service.UserService;
import com.authorization.life.auth.infra.entity.LifeUser;
import com.authorization.life.security.start.UserDetailService;
import com.authorization.redis.start.util.RedisCaptchaValidator;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.StringUtil;
import com.authorization.utils.security.SecurityCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户名密码登录的校验。
 */
@Slf4j
public class UsernamePasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    public static final String CAPTCHA_UUID = "captchaUuid";
    public static final String CAPTCHA_CODE = "captchaCode";
    public static final String CLIENT_ID = "client_id";

    private static final String PASSWORD_ERROR_COUNT = SecurityCoreService.PASSWORD_ERROR_COUNT_KEY;

    private final UserDetailService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final UserService userService;
    private final RegisteredClientRepository registeredClientService;

    public UsernamePasswordAuthenticationProvider(UserDetailService userDetailsService,
                                                  PasswordEncoder passwordEncoder,
                                                  RedisUtil redisUtil,
                                                  UserService userService,
                                                  RegisteredClientRepository registeredClientService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.redisUtil = redisUtil;
        this.userService = userService;
        this.registeredClientService = registeredClientService;
    }

    /**
     * 获取用户信息
     *
     * @param username       The username to retrieve
     * @param authentication The authentication request, which subclasses <em>may</em>
     *                       need to perform a binding-based retrieval of the <code>UserDetails</code>
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            Assert.notNull(user, () -> new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation"));
            Locale locale = LocaleContextHolder.getLocale();
            log.info("locale-->{}", locale);
            return user;
        } catch (UsernameNotFoundException notFound) {
            log.debug("User '" + username + "' not found");
            throw notFound;
        } catch (AuthenticationServiceException ex) {
            log.error("在自定义passwordAuthProvider的父类中验证失败，", ex);
            throw ex;
        }
    }

    /**
     * 其他身份检查, 例如, 用户是否已锁定, 用户是否过期,用户是否未激活
     *
     * @param userDetails    as retrieved from the
     *                       {@link #retrieveUser(String, UsernamePasswordAuthenticationToken)} or
     *                       <code>UserCache</code>
     * @param authentication the current request that needs to be authenticated
     * @throws AuthenticationException
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.info("additionalAuthenticationChecks-->{}", userDetails);
        // 检查是否存在验证码
        Object authenticationDetails = authentication.getDetails();
        if (authenticationDetails instanceof CaptchaWebAuthenticationDetails captcha && StrUtil.isNotBlank(captcha.getCaptchaCode())) {
            // 检查验证码正确
            boolean verify = RedisCaptchaValidator.verify(redisUtil, captcha.getCaptchaUuid(), captcha.getCaptchaCode());
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
            String cacheKey = StringUtil.of(PASSWORD_ERROR_COUNT).add("username", userDetails.getUsername()).format();
            redisUtil.delete(cacheKey);
        } else {
            log.debug("Authentication failed: password does not match stored value");
            // 检查密码错误次数
            String cacheKey = StringUtil.of(PASSWORD_ERROR_COUNT).add("username", userDetails.getUsername()).format();
            int passwordErrorCount = Optional.ofNullable(redisUtil.get(cacheKey)).map(Integer::parseInt).orElse(0);
            if (passwordErrorCount >= 5 && passwordErrorCount < 10) {
                // 未超过10次则密码错误累计次数+1
                redisUtil.setEx(cacheKey, String.valueOf(passwordErrorCount + 1), 10, TimeUnit.MINUTES);
                throw new VerificationCodeException("用户名或密码错误。");
            }
            if (passwordErrorCount >= 10) {
                // 输错10次则锁定用户3小时
                userService.lock(((LifeUser) userDetails).getUserId(), 3);
            } else {
                // 未超过10次则密码错误累计次数+1
                redisUtil.setEx(cacheKey, String.valueOf(passwordErrorCount + 1), 10, TimeUnit.MINUTES);
            }
            throw new BadCredentialsException("用户名或密码错误。");
        }
        // 检查登录端是否与用户组匹配
        if (authenticationDetails instanceof CaptchaWebAuthenticationDetails clientIdDetails && StrUtil.isNotBlank(clientIdDetails.getClientId())) {
            String clientId = clientIdDetails.getClientId();
            RegisteredClient registeredClient = registeredClientService.findByClientId(clientId);
            Assert.notNull(registeredClient, () -> new RegClientException("未找到此 OauthClient 信息。"));
            //检查授权域
            Set<String> allowedScopes = registeredClient.getScopes();
            //判断用户所在用户组与授权域是否相同
            boolean express = ((LifeUser) userDetails).getUserGroups().containsAll(allowedScopes);
            Assert.isTrue(express, () -> new InternalAuthenticationServiceException("用户名或密码错误"));
        }
    }

}
