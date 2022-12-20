package com.authorization.gateway.knife4j;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Knife4j 配置类
 *
 * @author wangjunming
 * @date 2022/12/20 20:41
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = "knife4j.gateway")
public class Knife4jGatewayProperties {

    /**
     * 是否启用聚合Swagger组件
     */
    private boolean enable = false;

    /**
     * 聚合分组名称
     */
    private List<Knife4jGatewayRoute> routes;

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 构建分组数据
     *
     * @return List<Map < String, String>>
     */
    public List<Map<String, String>> build() {
        List<String> services = discoveryClient.getServices();

        for (String serviceName : services) {
            log.info("serviceName-{}", serviceName);
        }

        List<Map<String, String>> dataMaps = new ArrayList<>();
        if (routes != null && routes.size() > 0) {
            for (Knife4jGatewayRoute route : this.routes) {
                Map<String, String> routeMap = new LinkedHashMap<>();
                routeMap.put("name", route.getName());
                routeMap.put("url", route.getUrl());
                String source = route.getName() + route.getUrl() + route.getServiceName();
                String id = Base64.getEncoder().encodeToString(source.getBytes(StandardCharsets.UTF_8));
                routeMap.put("id", id);
                dataMaps.add(routeMap);
            }
        }
        return dataMaps;
    }

}