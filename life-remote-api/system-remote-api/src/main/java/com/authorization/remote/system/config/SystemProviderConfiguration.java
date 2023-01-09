package com.authorization.remote.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * 服务提供暴露接口的配置类
 *
 * @author wangjunming
 * @date 2022/12/23 14:27
 */
@Configuration
@ImportResource("classpath:/remote/system-dubbo-provider.xml")
@PropertySource("classpath:/remote/system.properties")
public class SystemProviderConfiguration {


}
