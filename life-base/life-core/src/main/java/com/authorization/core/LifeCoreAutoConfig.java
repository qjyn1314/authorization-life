package com.authorization.core;

import cn.hutool.extra.spring.EnableSpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 公共配置类
 *
 * @author wangjunming
 */
@Slf4j
@AutoConfiguration
@EnableSpringUtil
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class LifeCoreAutoConfig {

}
