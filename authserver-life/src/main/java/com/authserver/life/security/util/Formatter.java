package com.authserver.life.security.util;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串占位符拼接工具类
 */
public final class Formatter {

    private final String pattern;
    private final Map<String, Object> kvMap = new HashMap<>();


    public static Formatter of(String pattern) {
        return new Formatter(pattern);
    }

    private Formatter(String pattern) {
        this.pattern = pattern;
    }

    public Formatter add(String key, Object value) {
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
