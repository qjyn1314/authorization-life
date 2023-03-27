package com.authorization.life.shardingsphere.app.service;


import com.authorization.life.shardingsphere.infra.entity.ConfDatasource;

import java.util.List;

/**
 * 数据源表
 *
 * @author code@code.com
 * @date 2023-02-24 13:29:22
 */
public interface ConfDatasourceService {

    List<ConfDatasource> selectDateSourceByServiceName(String serviceName);

}
