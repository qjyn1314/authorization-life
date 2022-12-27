package com.authorization.remote.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * 服务消费引用的配置类
 *
 * @author wangjunming
 * @date 2022/12/23 14:28
 */
@Configuration
@ImportResource("classpath:/remote/dubbo-consumer.xml")
@PropertySource("classpath:/remote/system.properties")
public class SystemConsumerConfiguration {


}