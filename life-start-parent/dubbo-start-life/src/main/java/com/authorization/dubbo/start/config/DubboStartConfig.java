package com.authorization.dubbo.start.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * dubbo3 的配置类
 * <p>
 * 此配置类参考:
 * <p>
 * 第一步: 参考此配置, 决定哪些配置是必须的:
 * <a href="https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/reference-manual/config/overview/">配置概述</a>
 * 第二步: 参考此配置, 决定参数是否需要配置:
 * <a href="https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/reference-manual/config/properties/">配置项参考手册</a>
 *
 * @author wangjunming
 * @date 2022/12/22 14:40
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@AutoConfiguration
public class DubboStartConfig {

    public static final String NAMESPACE = "namespace";
    public static final String CONSUMER_URL_SHOW_FLAG = "register-consumer-url";

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private Integer serverPort;

    public static final int QOS_PORT = 3;
    public static final int PROTOCOL_PORT = 5;

    public static final String OWNER = "authorization.life";

    /**
     * 所有dubbo服务, 接口, 需要在 dubbo分组中,与应用分组区分出来
     */
    public static final String GROUP_DUBBO = "dubbo";

    /**
     * 注册dubbo应用时的名称后缀, 需要与当前服务名称拼接
     */
    public static final String GROUP_DUBBO_ = "-" + GROUP_DUBBO;

    /**
     * 注册时指定注册的id,
     */
    public static final String REG_ID = "reg-nacos";

    /**
     * 服务注册到nacos注册中心时使用nacos协议
     */
    public static final String REG_PROTOCOL_NACOS = "nacos";

    /**
     * 服务之间调用使用dubbo协议
     */
    public static final String TRANSFER_PROTOCOL_DUBBO = "dubbo";

    public static final Integer TIMEOUT = 60000;

    /**
     * 必选
     * <p>
     * 每个应用必须要有且只有一个 application 配置，对应的配置类：org.apache.dubbo.config.ApplicationConfig
     * <p>
     * application	指定应用名等应用级别相关信息	一个应用内只允许出现一个
     */
    @Bean
    public ApplicationConfig dubboApplicationConfig(NacosDiscoveryProperties nacosDiscoveryProperties) {
        // QosPort 等于当前服务的端口号 + 3
        Integer qosPort = serverPort + QOS_PORT;
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(applicationName + GROUP_DUBBO_);
        applicationConfig.setOwner(OWNER);

        applicationConfig.setQosEnable(Boolean.TRUE);
        log.info("dubbo应用配置的应用服务qos端口号是-{}", qosPort);
        applicationConfig.setQosPort(qosPort);

        applicationConfig.setParameters(Map.of(NAMESPACE, nacosDiscoveryProperties.getNamespace()));
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
        // 注册中心地址协议(默认 dubbo)，支持dubbo, multicast, zookeeper, redis, consul(2.7.1), sofa(2.7.2), etcd(2.7.2), nacos(2.7.2)等协议
        registryConfig.setProtocol(REG_PROTOCOL_NACOS);
        registryConfig.setAddress(nacosDiscoveryProperties.getServerAddr());
        registryConfig.setUsername(nacosDiscoveryProperties.getUsername());
        registryConfig.setPassword(nacosDiscoveryProperties.getPassword());
        // 注册中心请求超时时间(毫秒) 默认 5000
        registryConfig.setTimeout(3000);
        // 注册中心会话超时时间(毫秒)，用于检测提供者非正常断线后的脏数据，比如用心跳检测的实现，此时间就是心跳间隔，不同注册中心实现不一样。默认 60000
        registryConfig.setSession(5000);
        // 服务注册分组，跨组的服务不会相互影响，也无法相互调用，适用于环境隔离。
        registryConfig.setGroup(GROUP_DUBBO);
        // 该注册中心是否作为配置中心使用
        registryConfig.setUseAsConfigCenter(Boolean.TRUE);
        // 该注册中心是否作为元数据中心使用
        registryConfig.setUseAsMetadataCenter(Boolean.TRUE);
        // 是否作为首选注册中心。当订阅多注册中心时，如果设为true，该注册中心作为首选
        registryConfig.setPreferred(Boolean.TRUE);
        // 客户端传输类型设置，如Dubbo协议的netty或mina。
        registryConfig.setClient("netty");

        Map<String, String> parameters = new java.util.HashMap<>(Map.of(NAMESPACE, nacosDiscoveryProperties.getNamespace()));
        // 消费者的url是否展示在nacos的服务列表中,默认不展示
        // 其服务名称示例(consumers:com.authorization.remote.system.service.SystemRemoteService:1.0.0:system-life-dubbo)为null
//        parameters.put(CONSUMER_URL_SHOW_FLAG, "true");
        registryConfig.setParameters(parameters);
        return registryConfig;
    }

    /**
     * 必选
     * <p>
     * registry	注册中心类型、地址及相关配置	一个应用内可配置多个，一个 registry 可作用于一组 service&reference
     * <p>
     * 服务提供者协议配置。对应的配置类： org.apache.dubbo.config.ProtocolConfig。
     * <p>
     * 同时，如果需要支持多协议，可以声明多个 <dubbo:protocol> 标签，并在 <dubbo:service> 中通过 protocol 属性指定使用的协议。
     */
    @Bean
    public ProtocolConfig dubboProtocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(TRANSFER_PROTOCOL_DUBBO);

        // 如果配置为-1，则会分配一个没有被占用的端口。Dubbo 2.4.0+，分配的端口在协议缺省端口的基础上增长，确保端口段可控。
        Integer protocolPort = serverPort + PROTOCOL_PORT;
        protocolConfig.setPort(protocolPort);

        // 设为true，将向logger中输出访问日志，也可填写访问日志文件路径，直接把访问日志输出到指定文件
        protocolConfig.setAccesslog("true");

        // 心跳间隔，对于长连接，当物理层断开时，比如拔网线，TCP的FIN消息来不及发送，对方收不到断开事件，此时需要心跳来帮助检查连接是否已断开
        protocolConfig.setHeartbeat(3000);
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
        // 启动时检查提供者是否存在，true报错，false忽略
        consumerConfig.setCheck(Boolean.FALSE);
        return consumerConfig;
    }

}
