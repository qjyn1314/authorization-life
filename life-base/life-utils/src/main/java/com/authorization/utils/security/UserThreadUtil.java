package com.authorization.utils.security;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * <p>
 * 此处是为了将当前登录用户的信息临时存放
 * </p>
 *
 * @author wangjunming
 * @since 2024-10-01 19:22
 */
public class UserThreadUtil {

    private static final TransmittableThreadLocal<UserDetail> USER_CONTEXT = new TransmittableThreadLocal<>();

    public static void setUserContext(final UserDetail userDetail) {
        USER_CONTEXT.set(userDetail);
    }

    public static UserDetail getUserContext() {
        return USER_CONTEXT.get();
    }

    public static void removeUserContext() {
        USER_CONTEXT.remove();
    }

}
