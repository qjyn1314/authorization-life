package com.authorization.dubbo.start.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * dubbo3 的配置类
 * <p>
 * 此配置类参考:
 * <p>
 * https://cn.dubbo.apache.org/zh/docs3-v2/java-sdk/reference-manual/config/properties/
 *
 * @author wangjunming
 * @date 2022/12/22 14:40
 */
@EnableDubbo
@Configuration
public class DubboStartConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    public static final String OWNER = "authorization";

    public static final String REG_ID = "nacos-reg";

    /**
     * 每个应用必须要有且只有一个 application 配置，对应的配置类：org.apache.dubbo.config.ApplicationConfig
     */
    @Bean
    public ApplicationConfig dubboApplicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(applicationName);
        applicationConfig.setOwner(OWNER);
        // 安全配置，是否接收除localhost本机访问之外的外部请求
        applicationConfig.setQosAcceptForeignIp(Boolean.TRUE);
        return applicationConfig;
    }

    /**
     * 注册中心配置。对应的配置类： org.apache.dubbo.config.RegistryConfig。
     * 同时如果有多个不同的注册中心，
     * 可以声明多个 <dubbo:registry> 标签，并在 <dubbo:service> 或 <dubbo:reference> 的 registry 属性指定使用的注册中心。
     */
    @Bean
    public RegistryConfig dubboRegistryConfig(NacosDiscoveryProperties nacosDiscoveryProperties){
        RegistryConfig registryConfig = new RegistryConfig();
        // 注册中心引用BeanId，可以在<dubbo:service registry="">或<dubbo:reference registry="">中引用此ID
        registryConfig.setId(REG_ID);
        registryConfig.setAddress(nacosDiscoveryProperties.getServerAddr());
//        registryConfig.setParameters();

        return registryConfig;
    }







}