package com.authorization.core;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.authorization.core.intercept.UrlFilter;
import com.authorization.core.intercept.WebMvcConfig;
import com.authorization.utils.json.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;

/**
 * 公共配置类
 *
 * @author wangjunming
 */
@Slf4j
@EnableSpringUtil
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class LifeCoreAutoConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        log.info("初始化自定义的ObjectMapperJson对象转换类...");
        return ObjectMappers.configMapper();
    }

    @Bean
    public WebMvcConfig webMvcConfig() {
        return new WebMvcConfig();
    }

    @Bean
    public UrlFilter urlFilter() {
        return new UrlFilter();
    }

}
