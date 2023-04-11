package com.authorization.life.system.infra.mapper;

import java.util.List;

import com.authorization.life.system.infra.entity.Tenant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 租户信息表 持久
 *
 * @author code@code.com 2023-04-11 11:14:24
 */
@Repository
public interface TenantMapper extends BaseMapper<Tenant> {

    List<Tenant> page(Tenant lifeTenant);

}
