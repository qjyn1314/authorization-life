package com.authorization.utils;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串占位符拼接工具类
 * String cacheKey = StringUtil.of("sso-oauth-server:auth:password-error-count:{username}").add("username", "zhangsan").format();
 * @author wangjunming
 */
public final class StringUtil extends StrUtil {

    private final String pattern;
    private final Map<String, Object> kvMap = new HashMap<>();


    public static StringUtil of(String pattern) {
        return new StringUtil(pattern);
    }

    private StringUtil(String pattern) {
        this.pattern = pattern;
    }

    public StringUtil add(String key, Object value) {
        this.kvMap.put(key, value);
        return this;
    }

    public String format() {
        return format(false);
    }

    public String format(boolean ignoreNull) {
        return StrUtil.format(this.pattern, kvMap, ignoreNull);
    }
}
