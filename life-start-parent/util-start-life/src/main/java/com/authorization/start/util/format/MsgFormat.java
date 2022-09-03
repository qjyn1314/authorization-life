package com.authorization.start.util.format;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串占位符拼接工具类
 */
public final class MsgFormat {

    private final String pattern;
    private final Map<String, Object> kvMap = new HashMap<>();


    public static MsgFormat of(String pattern) {
        return new MsgFormat(pattern);
    }

    private MsgFormat(String pattern) {
        this.pattern = pattern;
    }

    public MsgFormat add(String key, Object value) {
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
