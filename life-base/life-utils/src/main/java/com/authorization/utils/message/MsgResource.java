package com.authorization.utils.message;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * 国际化提示消息处理器
 * <p>
 * 参考:
 * https://elim168.github.io/spring/bean/21.%E5%9B%BD%E9%99%85%E5%8C%96MessageSource.html
 *
 * @author wangjunming
 * @date 2023/3/7 14:28
 */
public class MsgResource {

    private static final ReloadableResourceBundleMessageSource BUNDLE_MESSAGE_SOURCE;

    public static final String[] BASE_NAMES = new String[]{
            "classpath:message/messages_util",
            "classpath:message/messages_core",
    };

    static {
        BUNDLE_MESSAGE_SOURCE = new ReloadableResourceBundleMessageSource();
        BUNDLE_MESSAGE_SOURCE.setBasenames(BASE_NAMES);
        BUNDLE_MESSAGE_SOURCE.setDefaultEncoding("UTF-8");
    }

    /**
     * 添加资源文件位置
     *
     * @param names 如 <code>classpath:messages/messages_core</code>
     */
    public static void addBasenames(String... names) {
        BUNDLE_MESSAGE_SOURCE.addBasenames(names);
    }

    /**
     * 覆盖默认资源文件位置
     *
     * @param names 如 <code>classpath:messages/messages_core</code>
     */
    public static void setBasenames(String... names) {
        BUNDLE_MESSAGE_SOURCE.setBasenames(names);
    }

    /**
     * 从本地消息文件获取多语言消息
     */
    public static Msg getMessageLocal(String code, Object[] args, Locale locale) {
        String message = BUNDLE_MESSAGE_SOURCE.getMessage(code, args, locale);
        return new Msg().setCode(code).setMessage(message);
    }

    /**
     * 从本地消息文件获取多语言消息
     */
    public static String getMsg(String code, Locale locale) {
        return BUNDLE_MESSAGE_SOURCE.getMessage(code, null, Locale.getDefault());
    }

    /**
     * 从本地消息文件获取多语言消息
     */
    public static String getMsg(String code, Object[] args) {
        return BUNDLE_MESSAGE_SOURCE.getMessage(code, args, Locale.getDefault());
    }

    /**
     * 从本地消息文件获取多语言消息
     */
    public static String getMsg(String code) {
        return BUNDLE_MESSAGE_SOURCE.getMessage(code, null, Locale.getDefault());
    }

}
