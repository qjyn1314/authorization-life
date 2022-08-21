package com.authorization.core.entity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserHelper {

    public static UserDetail getUserDetail() {
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return principal instanceof UserDetail ? (UserDetail)principal : null;
        } else {
            return null;
        }
    }

    public static void setUserDetail(UserDetail userInfo) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userInfo, (Object)null);
            securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        }

    }
}
