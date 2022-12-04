package com.authorization.common.excel;

import java.lang.annotation.*;

/**
 * 解析excel用到的注解。
 * @author wangjunming
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelDecorate {

    /**
     * 标题名
     */
    String[] value() default {""};

    /**
     * 所属于第几列
     */
    int index() default -1;

    /**
     * 是否必填
     */
    boolean required() default false;

    /**
     * 字段批注
     */
    String fieldNotes() default "";

}
