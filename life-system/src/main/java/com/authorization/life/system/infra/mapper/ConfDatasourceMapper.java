package com.authorization.life.system.infra.mapper;

import com.authorization.life.system.infra.entity.ConfDatasource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据源表 持久
 *
 * @author code@code.com 2023-04-07 18:00:09
 */
@Repository
public interface ConfDatasourceMapper extends BaseMapper<ConfDatasource> {

    List<ConfDatasource> page(ConfDatasource confDatasource);

}
