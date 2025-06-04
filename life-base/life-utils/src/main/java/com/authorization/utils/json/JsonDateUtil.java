package com.authorization.utils.json;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class JsonDateUtil extends DateUtil {

    private JsonDateUtil() {
        throw new IllegalArgumentException("此处不允许创建对象。");
    }

    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ
     */
    public static final String DATETIME_SSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    /**
     * yyyy-MM-dd
     */
    public static final String DATE = "yyyy-MM-dd";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String DATETIME_MM = "yyyy-MM-dd HH:mm";
    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String DATETIME_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * HH:mm
     */
    public static final String TIME = "HH:mm";
    /**
     * HH:mm:ss
     */
    public static final String TIME_SS = "HH:mm:ss";

    // 系统时间格式
    // ----------------------------------------------------------------------------------------------------
    /**
     * yyyy/MM/dd
     */
    public static final String SYS_DATE = "yyyy/MM/dd";
    /**
     * yyyy/MM/dd HH:mm:ss
     */
    public static final String SYS_DATETIME = "yyyy/MM/dd HH:mm:ss";
    /**
     * yyyy/MM/dd HH:mm
     */
    public static final String SYS_DATETIME_MM = "yyyy/MM/dd HH:mm";
    /**
     * yyyy/MM/dd HH:mm:ss.SSS
     */
    public static final String SYS_DATETIME_SSS = "yyyy/MM/dd HH:mm:ss.SSS";

    // 无连接符模式
    // ----------------------------------------------------------------------------------------------------
    /**
     * yyyyMMdd
     */
    public static final String NONE_DATE = "yyyyMMdd";
    /**
     * yyyyMMddHHmmss
     */
    public static final String NONE_DATETIME = "yyyyMMddHHmmss";
    /**
     * yyyyMMddHHmm
     */
    public static final String NONE_DATETIME_MM = "yyyyMMddHHmm";
    /**
     * yyyyMMddHHmmssSSS
     */
    public static final String NONE_DATETIME_SSS = "yyyyMMddHHmmssSSS";

    /**
     * 将毫秒转换为秒和分
     */
    public static String formatMillis(long milliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%d 分 %d 秒", minutes, seconds);
    }

}
