package com.authorization.gateway.knife4j;

import cn.hutool.core.map.MapUtil;
import com.authorization.utils.contsant.ServerInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
        if (CollectionUtils.isEmpty(services)) {
            return new ArrayList<>();
        }
        return services.stream()
                .filter(service -> !ServerInfo.GateWayLife.SERVER_NAME.equals(service))
                .map(this::convert).collect(Collectors.toList());
    }

    private Map<String, String> convert(String service) {
        String url = "/" + service + "/v3/api-docs/" + service + "?group=APPLICATION";
        String source = service + url + service;
        return MapUtil.builder("name", service)
                .put("url", url)
                .put("id", Base64.getEncoder().encodeToString(source.getBytes(StandardCharsets.UTF_8)))
                .build();
    }

}