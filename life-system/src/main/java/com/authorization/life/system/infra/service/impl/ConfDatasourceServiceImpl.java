package com.authorization.life.system.infra.service.impl;

import com.authorization.life.system.infra.entity.ConfDatasource;
import com.authorization.life.system.infra.mapper.ConfDatasourceMapper;
import com.authorization.life.system.infra.service.ConfDatasourceService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数据源表
 *
 * @author code@code.com
 * @date 2023-04-07 18:00:09
 */
@Service
public class ConfDatasourceServiceImpl implements ConfDatasourceService {

    @Autowired
    private ConfDatasourceMapper mapper;



}
