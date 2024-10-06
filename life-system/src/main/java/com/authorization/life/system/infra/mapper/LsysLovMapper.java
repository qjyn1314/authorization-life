package com.authorization.life.system.infra.mapper;

import java.util.List;

import com.authorization.life.system.infra.entity.LsysLov;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 字典主表 持久
 *
 * @author code@code.com 2024-10-05 19:21:32
 */
@Repository
public interface LsysLovMapper extends BaseMapper<LsysLov> {

    List<LsysLov> page(LsysLov lsysLov);

}
