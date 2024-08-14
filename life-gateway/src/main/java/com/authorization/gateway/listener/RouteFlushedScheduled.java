package com.authorization.gateway.listener;

import com.authorization.gateway.service.DynamicRoutingService;
import com.authorization.gateway.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2024-08-14 16:44
 */
@Slf4j
@Component
public class RouteFlushedScheduled {

    @Autowired
    private RouteService routeService;
    @Autowired
    private DynamicRoutingService dynamicRoutingService;
    @Autowired
    private RouteDefinitionRepository routeDefinitionRepository;

    /**
     * 定时任务进行触发gateway路由刷新
     */
    @Scheduled(cron = "0/3 * * * * ?")
    public void redisNewsRelease() {
        dynamicRoutingService.updateRoutesByPublishRefreshRoutesEvent();
        routeService.refreshRoutes(null);
    }

}
