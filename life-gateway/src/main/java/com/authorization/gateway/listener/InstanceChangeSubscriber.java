package com.authorization.gateway.listener;

import com.authorization.gateway.service.DynamicRoutingService;
import com.authorization.gateway.service.RouteService;
import com.authorization.redis.start.listener.RedisSubscription;
import com.authorization.utils.contsant.ServerUpDown;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 进行订阅，服务上线频道，
 * https://github.com/shimmerjordan/sz-education/blob/c3ca5ecfb34aa91484b06fc01f0a0665b1460822/gateway-service/src/main/java/com/github/shimmerjordan/gateway/service/DynamicRouteService.java
 * 需要调用gateway的路由刷新事件
 *
 * @author 订阅redis事件配置类.
 */
@Slf4j
@Component
public class InstanceChangeSubscriber implements RedisSubscription {

    @Autowired
    private RouteService routeService;
    @Autowired
    private NamedContextFactory<LoadBalancerClientSpecification> contextFactory;
    @Autowired
    private DynamicRoutingService dynamicRoutingService;

    /**
     * 此处为订阅到了 redis发布的服务上线事件
     *
     * @param instanceChangeHeader 事件名称
     * @param applicationName      服务名称
     */
    @Override
    public void subscribe(String instanceChangeHeader, String applicationName) {
        log.debug("[{}] 服务实例变动, 事件为: [{}], 触发路由刷新及实例快照清理", applicationName, instanceChangeHeader);
        dynamicRoutingService.updateRoutesByPublishRefreshRoutesEvent();
        routeService.refreshRoutes(null);
        // 如果存在服务上下文，则清除实例快照缓存
        if (contextFactory.getContextNames().contains(applicationName)) {
            LoadBalancerCacheManager cacheManager = contextFactory.getInstance(applicationName, LoadBalancerCacheManager.class);
            Cache cache = cacheManager.getCache(CachingServiceInstanceListSupplier.SERVICE_INSTANCE_CACHE_NAME);
            if (Objects.nonNull(cache)) {
                cache.evictIfPresent(applicationName);
            }
        }
    }

    @Override
    public String topicName() {
        return ServerUpDown.INSTANCE_CHANNEL;
    }

    @Override
    public Topic topic() {
        return new PatternTopic(topicName());
    }

}
