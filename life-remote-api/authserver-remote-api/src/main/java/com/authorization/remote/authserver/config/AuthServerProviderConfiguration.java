package com.authorization.remote.authserver.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * 服务提供暴露接口的配置类
 *
 * @author wangjunming
 * @date 2022/12/23 14:27
 */
@AutoConfiguration
@ImportResource("classpath:/remote/authserver-dubbo-provider.xml")
@PropertySource("classpath:/remote/authserver.properties")
public class AuthServerProviderConfiguration {


}
