package com.authorization.utils.security;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Objects;

/**
 * <p>
 * 此处是为了将当前登录用户的信息临时存放
 * </p>
 *
 * @author wangjunming
 * @since 2024-10-01 19:22
 */
public class UserDetailUtil {

    private static final TransmittableThreadLocal<UserDetail> USER_CONTEXT = new TransmittableThreadLocal<>();

    public static void setUserContext(final UserDetail userDetail) {
        USER_CONTEXT.set(userDetail);
    }

    public static UserDetail getUserContext() {
        return USER_CONTEXT.get();
    }

    public static void remove() {
        if (Objects.nonNull(USER_CONTEXT.get())) {
            USER_CONTEXT.remove();
        }
    }

}
