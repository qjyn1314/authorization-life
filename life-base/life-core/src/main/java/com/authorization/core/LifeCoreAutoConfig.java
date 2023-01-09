package com.authorization.core;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 公共配置类
 *
 * @author wangjunming
 */
@AutoConfiguration
@EnableSpringUtil
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class LifeCoreAutoConfig {


}
