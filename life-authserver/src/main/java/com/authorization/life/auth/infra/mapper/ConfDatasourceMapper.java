package com.authorization.life.auth.infra.mapper;

import java.util.List;

import com.authorization.life.auth.infra.entity.ConfDatasource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 数据源表 持久
 *
 * @author code@code.com 2023-02-24 13:29:22
 */
@Repository
public interface ConfDatasourceMapper extends BaseMapper<ConfDatasource> {

    List<ConfDatasource> page(ConfDatasource confdatasource);

}
