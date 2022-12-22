package com.authorization.core;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 公共配置类
 *
 * @author wangjunming
 */
@Configuration
@EnableSpringUtil
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class LifeCoreAutoConfig {


}
