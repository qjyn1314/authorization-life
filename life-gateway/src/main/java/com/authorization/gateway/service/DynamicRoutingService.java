package com.authorization.gateway.service;

import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * 用于发送 gateway 路由刷新事件服务, 即在订阅到redis的发布上线服务的时候,调用此服务进行刷新gateway路由.
 *
 * @author wangjunming
 */
@Component
public class DynamicRoutingService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 此处参考:
     * <p>
     * https://blog.csdn.net/qq_31349087/article/details/120567227
     * <p>
     * 调用gateway的路由刷新事件
     */
    public void updateRoutesByPublishRefreshRoutesEvent() {
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }


}
