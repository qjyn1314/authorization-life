package com.authorization.life.auth.app.service.impl;

import com.authorization.life.auth.app.service.ConfDatasourceService;
import com.authorization.life.auth.infra.mapper.ConfDatasourceMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数据源表
 *
 * @author code@code.com
 * @date 2023-02-24 13:29:22
 */
@Service
public class ConfDatasourceServiceImpl implements ConfDatasourceService {

    @Autowired
    private ConfDatasourceMapper mapper;



}
