package com.authorization.gateway.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.gateway.filter.AuthGatewayFilterFactory;
import com.authorization.gateway.filter.JwtTokenGatewayFilterFactory;
import com.authorization.gateway.filter.UrlResolveGatewayFilterFactory;
import com.authorization.gateway.service.RouteService;
import com.authorization.utils.contsant.ServerOnlineConstants;
import com.authorization.utils.contsant.ServerInfo;
import com.authorization.utils.json.JsonHelper;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerResilience4JFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RouteServiceImpl implements RouteDefinitionRepository, RouteService {

    private List<RouteDefinition> routes = Collections.emptyList();
    private final List<RouteDefinition> staticRoutes;

    @Autowired
    private DiscoveryClient discoveryClient;

    public RouteServiceImpl(GatewayProperties gatewayProperties) {
        this.staticRoutes = Optional.ofNullable(gatewayProperties.getRoutes())
                .orElse(Collections.emptyList());
    }

    @Override
    @EventListener(RefreshRoutesEvent.class)
    public void refreshRoutes(RefreshRoutesEvent refreshRoutesEvent) {
        log.info("开始主动的刷新路由......");
        List<String> services = discoveryClient.getServices();
        List<RouteDefinition> mergeRoutes = CollUtil.newArrayList(services.stream()
                .map(this::convert)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        mergeRoutes.addAll(this.staticRoutes);
        routes = ImmutableList.copyOf(mergeRoutes);
        log.trace("[DynamicRoute] current routes: [{}]", JsonHelper.writeValueAsString(mergeRoutes));
    }

    /**
     * @param service 即路由路径名
     * @return RouteDefinition
     */
    private RouteDefinition convert(String service) {
        // 不注册网关服务路由
        if (Objects.equals(service, ServerInfo.GateWayLife.SERVER_NAME)) {
            return null;
        }
        List<ServiceInstance> instances = discoveryClient.getInstances(service);
        ServiceInstance instance = instances.stream().findFirst().orElse(null);
        if (Objects.isNull(instance)) {
            log.warn("[DynamicRoute] [{}] unavailable instance! dynamic route will ignore", service);
            return null;
        }
        log.debug("服务-instance-{}", JSONUtil.toJsonStr(instance.getMetadata()));
        String serviceName = instance.getMetadata().getOrDefault(ServerOnlineConstants.KEY_SERVICE_CODE, service);
        log.debug("服务-instance-serviceName-{}", serviceName);
        log.debug("服务-instance-serviceId-{}", instance.getServiceId());
        //此处将使用服务名称作为请求路径前缀进行处理请求。
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(service);
        routeDefinition.setUri(buildUri(service));
        routeDefinition.setPredicates(buildPredicates(serviceName));
        routeDefinition.setFilters(buildFilters());
        return routeDefinition;
    }

    private URI buildUri(String service) {
        return URI.create("lb://" + service);
    }

    private List<PredicateDefinition> buildPredicates(String serviceName) {
        // 获取服务名称作为路由匹配前缀
        String path = "/" + serviceName + "/**";
        PredicateDefinition predicateDefinition = new PredicateDefinition("Path=" + path);
        return CollUtil.newArrayList(predicateDefinition);
    }

    private List<FilterDefinition> buildFilters() {
        return CollUtil.newArrayList(
                new FilterDefinition(UrlResolveGatewayFilterFactory.URL_RESOLVE),
                new FilterDefinition(JwtTokenGatewayFilterFactory.JWT_TOKEN),
                new FilterDefinition(AuthGatewayFilterFactory.AUTH),
                new FilterDefinition(SpringCloudCircuitBreakerResilience4JFilterFactory.NAME));
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }


}
