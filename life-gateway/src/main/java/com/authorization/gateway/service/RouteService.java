package com.authorization.gateway.service;

import org.springframework.cloud.gateway.event.RefreshRoutesEvent;

public interface RouteService {

    /**
     * 刷新权限
     */
    void refreshRoutes(RefreshRoutesEvent refreshRoutesEvent);

}
