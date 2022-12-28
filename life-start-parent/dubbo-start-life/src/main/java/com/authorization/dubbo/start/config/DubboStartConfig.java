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
@Configuration(proxyBeanMethods = false)
public class DubboStartConfig {

    public static final String NAMESPACE = "namespace";

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private Integer serverPort;

    public static final String OWNER = "authorization";

    public static final String GROUP_DUBBO = "dubbo";

    public static final String GROUP_DUBBO_ = "-" + GROUP_DUBBO;

    public static final String REG_ID = "nacos";

    public static final String REG_PROTOCOL_NACOS = "nacos";

    public static final String PROTOCOL_DUBBO = "dubbo";

    public static final Integer TIMEOUT = 60000;

    /**
     * 每个应用必须要有且只有一个 application 配置，对应的配置类：org.apache.dubbo.config.ApplicationConfig
     */
    @Bean
    public ApplicationConfig dubboApplicationConfig() {
        // QosPort 等于当前服务的端口号 + 3
        Integer qosPort = serverPort + 3;
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(applicationName + GROUP_DUBBO_);
        applicationConfig.setOwner(OWNER);
        applicationConfig.setQosEnable(Boolean.TRUE);
        applicationConfig.setQosPort(qosPort);
        applicationConfig.setQosAcceptForeignIp(Boolean.TRUE);
        applicationConfig.setMetadataType("remote");
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
        registryConfig.setId(REG_ID);
        registryConfig.setGroup(GROUP_DUBBO);
        registryConfig.setProtocol(REG_PROTOCOL_NACOS);
        registryConfig.setAddress(nacosDiscoveryProperties.getServerAddr());
        Map<String, String> parameters = new java.util.HashMap<>(Map.of(NAMESPACE, nacosDiscoveryProperties.getNamespace()));
        parameters.put("register-consumer-url","true");
        registryConfig.setParameters(parameters);
        registryConfig.setEnableEmptyProtection(Boolean.TRUE);
        registryConfig.setCheck(Boolean.FALSE);
        return registryConfig;
    }


    /**
     * 配置中心。对应的配置类：org.apache.dubbo.config.ConfigCenterConfig
     */
    @Bean
    public ConfigCenterConfig dubboConfigCenterConfig(NacosDiscoveryProperties nacosDiscoveryProperties) {
        ConfigCenterConfig configCenterConfig = new ConfigCenterConfig();
        configCenterConfig.setGroup(GROUP_DUBBO);
        configCenterConfig.setProtocol(REG_PROTOCOL_NACOS);
        configCenterConfig.setNamespace(nacosDiscoveryProperties.getNamespace());
        configCenterConfig.setAddress(nacosDiscoveryProperties.getServerAddr());
        configCenterConfig.setCheck(Boolean.FALSE);
        return configCenterConfig;
    }

    /**
     * 元数据中心。对应的配置类：org.apache.dubbo.config.MetadataReportConfig
     */
    @Bean
    public MetadataReportConfig dubboMetadataReportConfig(NacosDiscoveryProperties nacosDiscoveryProperties) {
        MetadataReportConfig metadataReportConfig = new MetadataReportConfig();
        metadataReportConfig.setGroup(GROUP_DUBBO);
        metadataReportConfig.setProtocol(REG_PROTOCOL_NACOS);
        metadataReportConfig.setAddress(nacosDiscoveryProperties.getServerAddr());
        metadataReportConfig.setParameters(Map.of(NAMESPACE, nacosDiscoveryProperties.getNamespace()));
        return metadataReportConfig;
    }

    /**
     * 服务提供者协议配置。对应的配置类： org.apache.dubbo.config.ProtocolConfig。
     * 同时，如果需要支持多协议，可以声明多个 <dubbo:protocol> 标签，并在 <dubbo:service> 中通过 protocol 属性指定使用的协议。
     */
    @Bean
    public ProtocolConfig dubboProtocolConfig() {

        Integer protocolPort = serverPort + 5;

        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(PROTOCOL_DUBBO);
        // 如果配置为-1，则会分配一个没有被占用的端口。Dubbo 2.4.0+，分配的端口在协议缺省端口的基础上增长，确保端口段可控。
        protocolConfig.setPort(protocolPort);
        // 设为true，将向logger中输出访问日志，
        protocolConfig.setAccesslog("true");
        return protocolConfig;
    }

    /**
     * 服务提供者缺省值配置。对应的配置类： org.apache.dubbo.config.ProviderConfig。
     * 同时该标签为 <dubbo:service> 和 <dubbo:protocol> 标签的缺省值设置。
     */
    @Bean
    public ProviderConfig dubboProviderConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(TIMEOUT);
        return providerConfig;
    }

    /**
     * 服务消费者缺省值配置。配置类： org.apache.dubbo.config.ConsumerConfig 。
     * 同时该标签为 <dubbo:reference> 标签的缺省值设置。
     */
    @Bean
    public ConsumerConfig dubboConsumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setTimeout(TIMEOUT);
        return consumerConfig;
    }

}