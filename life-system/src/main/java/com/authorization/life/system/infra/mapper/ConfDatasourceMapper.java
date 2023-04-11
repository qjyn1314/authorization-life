package com.authorization.life.system.infra.mapper;

import java.util.List;

import com.authorization.life.system.infra.entity.ConfDatasource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 数据源表 持久
 *
 * @author code@code.com 2023-04-11 11:06:33
 */
@Repository
public interface ConfDatasourceMapper extends BaseMapper<ConfDatasource> {

    List<ConfDatasource> page(ConfDatasource confDatasource);

}
