package com.authorization.life.system.infra.mapper;

import java.util.List;

import com.authorization.life.system.infra.entity.LsysTempUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 模板接收人员 持久
 *
 * @author code@code.com 2024-10-01 23:08:40
 */
@Repository
public interface LsysTempUserMapper extends BaseMapper<LsysTempUser> {

    List<LsysTempUser> page(LsysTempUser lsysTempUser);

}
