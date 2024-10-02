package com.authorization.life.system.infra.mapper;

import java.util.List;

import com.authorization.life.system.infra.entity.LsysTemp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 模版 持久
 *
 * @author code@code.com 2024-10-01 23:07:37
 */
@Repository
public interface LsysTempMapper extends BaseMapper<LsysTemp> {

    List<LsysTemp> page(LsysTemp lsysTemp);

}
