package com.authorization.core.exception.handle;

import com.authorization.core.exception.vo.ExceptionDefaultMsg;
import com.authorization.core.exception.vo.ExceptionErrorCode;

/**
 * 默认的 错误消息常量类
 *
 * @author wangjunming
 * @date 2023/3/7 17:34
 */
public class DefaultErrorMsg {

    /**
     * 未登录, 获取不到当前登录用户信息.
     */
    public static final ExceptionErrorCode USER_NOT_LOGIN = ExceptionDefaultMsg.of("life-core.user-not-logined");
    /**
     * 当前登录过期，请重新登录。
     */
    public static final ExceptionErrorCode EXPIRED_STRATEGY_MSG = ExceptionDefaultMsg.of("life-core.user-login-expired");
    /**
     * 未找到请求头的token，请确认已登录。
     */
    public static final ExceptionErrorCode TOKEN_OF_HEADER_NOT_FOUND = ExceptionDefaultMsg.of("life-core.token-of-header-not-found");
    /**
     * 系统异常，请稍后重试。
     */
    public static final ExceptionErrorCode SYSTEM_EXCEPTION = ExceptionDefaultMsg.of("life-core.system-exception");

}
