package com.authorization.utils.kvp;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串占位符拼接工具类
 * @author wangjunming
 */
public final class KvpFormat {

    private final String pattern;
    private final Map<String, Object> kvMap = new HashMap<>();


    public static KvpFormat of(String pattern) {
        return new KvpFormat(pattern);
    }

    private KvpFormat(String pattern) {
        this.pattern = pattern;
    }

    public KvpFormat add(String key, Object value) {
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
