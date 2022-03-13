//package com.authserver.common.life.security.service;
//
//import cn.hutool.core.lang.UUID;
//import com.authserver.common.life.entity.UserDetail;
//import com.authserver.common.life.security.SecurityContant;
//import com.authserver.common.life.security.util.Formatter;
//import com.authserver.common.life.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
//import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
//
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//public class UserDetailCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
//
//    private final UserService userDetailService;
//    private final RedisTemplate<String, Object> redisHelper;
//
//    public UserDetailCustomizer(UserService userDetailService, RedisTemplate<String, Object> redisHelper) {
//        this.userDetailService = userDetailService;
//        this.redisHelper = redisHelper;
//    }
//
//    @Override
//    public void customize(JwtEncodingContext context) {
//        if (context.getPrincipal().getPrincipal() instanceof UserDetails) {
//            UserDetail userDetail = userDetailService.createUserDetailByUser((UserDetails) context.getPrincipal().getPrincipal());
//            String token = UUID.randomUUID().toString(true);
//            userDetail.setToken(token);
//            redisHelper.opsForValue().set(Formatter.of(SecurityContant.TOKEN_STORE).add("userId", userDetail.getUserId().toString()).format(), token);
//            redisHelper.opsForValue().set(Formatter.of(SecurityContant.USER_DETAIL).add("token", token).format(), userDetail,
//                    context.getRegisteredClient().getTokenSettings().getAccessTokenTimeToLive().getSeconds(), TimeUnit.SECONDS);
//            context.getClaims().claim("token", token).build();
//        }
//    }
//}
