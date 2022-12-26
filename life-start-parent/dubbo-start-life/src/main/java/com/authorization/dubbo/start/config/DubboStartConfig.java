package com.authorization.dubbo.start.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.apache.dubbo.config.*;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

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

    public static final String NACOS_REG_ID = "nacos-reg";

    public static final String NAMESPACE = "namespace";

    public static final String NACOS_PROTOCOL = "nacos";

    public static final String PROTOCOL_DUBBO = "dubbo";

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
    public RegistryConfig dubboRegistryConfig(NacosDiscoveryProperties nacosDiscoveryProperties) {
        RegistryConfig registryConfig = new RegistryConfig();
        // 注册中心引用BeanId，可以在<dubbo:service registry="">或<dubbo:reference registry="">中引用此ID
        registryConfig.setId(NACOS_REG_ID);
        registryConfig.setAddress(nacosDiscoveryProperties.getServerAddr());
        registryConfig.setParameters(Map.of(NAMESPACE, nacosDiscoveryProperties.getNamespace()));
        return registryConfig;
    }

    /**
     * 配置中心。对应的配置类：org.apache.dubbo.config.ConfigCenterConfig
     */
    @Bean
    public ConfigCenterConfig dubboConfigCenterConfig(NacosDiscoveryProperties nacosDiscoveryProperties) {
        ConfigCenterConfig configCenterConfig = new ConfigCenterConfig();
        configCenterConfig.setProtocol(NACOS_PROTOCOL);
        configCenterConfig.setAddress(nacosDiscoveryProperties.getServerAddr());
        configCenterConfig.setGroup(NACOS_REG_ID);
        return configCenterConfig;
    }

    /**
     * 元数据中心。对应的配置类：org.apache.dubbo.config.MetadataReportConfig
     */
    @Bean
    public MetadataReportConfig dubboMetadataReportConfig(NacosDiscoveryProperties nacosDiscoveryProperties) {
        MetadataReportConfig metadataReportConfig = new MetadataReportConfig();
        metadataReportConfig.setProtocol(NACOS_PROTOCOL);
        metadataReportConfig.setAddress(nacosDiscoveryProperties.getServerAddr());
        metadataReportConfig.setGroup(NACOS_REG_ID);
        return metadataReportConfig;
    }

    /**
     * 服务提供者协议配置。对应的配置类： org.apache.dubbo.config.ProtocolConfig。
     * 同时，如果需要支持多协议，可以声明多个 <dubbo:protocol> 标签，并在 <dubbo:service> 中通过 protocol 属性指定使用的协议。
     */
    @Bean
    public ProtocolConfig dubboProtocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(PROTOCOL_DUBBO);
        // 如果配置为-1，则会分配一个没有被占用的端口。Dubbo 2.4.0+，分配的端口在协议缺省端口的基础上增长，确保端口段可控。
        protocolConfig.setPort(-1);
        // 设为true，将向logger中输出访问日志，
        protocolConfig.setAccesslog("true");
        return protocolConfig;
    }

}