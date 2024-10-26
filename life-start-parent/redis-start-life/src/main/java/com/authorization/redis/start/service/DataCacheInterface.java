package com.authorization.redis.start.service;

public interface DataCacheInterface<T> {

    /**
     * 实现数据缓存映射
     *
     * @param tenantId 租户id
     * @return T 返回具体定义
     * @throws Throwable
     */
    <T> T getDbData(String tenantId);

}
