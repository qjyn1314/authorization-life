package com.authorization.core.security.entity;

import com.authorization.utils.security.UserDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class UserHelper {

    public static UserDetail getUserDetail() {
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return principal instanceof UserDetail ? (UserDetail) principal : null;
        } else {
            return null;
        }
    }

    public static void setUserDetail(UserDetail userInfo) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (Objects.isNull(securityContext) || Objects.isNull(userInfo)) {
            return;
        }
        // 如果是匿名用户则设置为未认证用户
        if (userInfo.getAnonymousFlag()) {
            securityContext.setAuthentication(UsernamePasswordAuthenticationToken.unauthenticated(userInfo, null));
        } else {
            securityContext.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(userInfo, null, null));
        }
    }
}
