package com.authorization.common.log;

import java.lang.annotation.*;

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
@Documented
public @interface LogAdvice {

    /**方法名*/
    String name() default "";

}
