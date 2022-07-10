package com.authserver.use.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * aop的日志注解
 * </p>
 *
 * @author wangjunming
 * @since 2022/3/27 13:27
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAdvice {

    /**方法名*/
    String name() default "";

}
