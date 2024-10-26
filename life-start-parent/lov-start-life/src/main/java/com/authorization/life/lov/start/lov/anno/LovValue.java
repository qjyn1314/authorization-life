package com.authorization.life.lov.start.lov.anno;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 标记在需要值集处理的字段上
 * @author wangjunming
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface LovValue {

    /**
     * @return 值集值所在的值集代码
     * @see #lovCode()
     */
    @AliasFor("lovCode")
    String value() default "";

    /**
     * @return 值集值所在的值集代码
     */
    @AliasFor("value")
    String lovCode() default "";

    /**
     * 处理后的含义字段放在哪个字段中
     * 不设置则进行默认映射,将注解所在字段名末尾自动添加Content,例:
     * statusCode -> statusCodeContent
     * processStatus -> processStatusContent
     * codeTypeCode -> codeTypeCodeContent
     */
    String contentField() default "";

    /**
     * @return 如果处理失败, 含义字段默认设置的值, 默认为原value
     */
    String defaultContent() default "";


    /**
     * 是否是必填项：true-必填项（才做效验）；false-不是必填项；
     */
    boolean required() default true;

    /**
     * 默认提示消息
     */
    String message() default "当前选项不符合规则，请重新选择。";

    /**
     * 需要验证的分组
     */
    Class<?>[] groups() default {};

}
