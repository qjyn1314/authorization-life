package com.authorization.core.proxy;

import org.springframework.aop.framework.AopContext;

/**
 * 获取当前接口的代理对象
 *
 * @author wangjunming
 * @date 2022/12/30 13:41
 */
public interface LifeProxy<T> {

    @SuppressWarnings("unchecked")
    default T self(){
        return (T) AopContext.currentProxy();
    }

}
