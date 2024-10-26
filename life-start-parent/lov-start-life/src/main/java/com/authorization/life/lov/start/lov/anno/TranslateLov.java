package com.authorization.life.lov.start.lov.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 值集翻译拦截注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TranslateLov {

    /**
     * @return 指定翻译目标，默认在当前对象中进行翻译
     */
    String[] targetField() default "";
}
