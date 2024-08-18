package com.authorization.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 定时任务触发gateway路由刷新事件
 * </p>
 *
 * @author wangjunming
 * @since 2024-08-18 13:18
 */
@Component
public class DynamicRoutingScheduled {

    @Autowired
    private DynamicRoutingService dynamicRoutingService;
    @Autowired
    private RouteService routeService;

    @Scheduled(cron = "0/2 * * * * ?")
    public void flushedRoute() {
        dynamicRoutingService.updateRoutesByPublishRefreshRoutesEvent();
        routeService.refreshRoutes(null);
    }

}
