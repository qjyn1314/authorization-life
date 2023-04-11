package com.authorization.life.system.infra.service.impl;

import com.authorization.life.system.infra.entity.Tenant;
import com.authorization.life.system.infra.mapper.TenantMapper;
import com.authorization.life.system.infra.service.TenantService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 租户信息表
 *
 * @author code@code.com
 * @date 2023-04-11 11:14:24
 */
@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantMapper mapper;



}
