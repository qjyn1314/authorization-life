package com.authorization.life.system.infra.mapper;

import java.util.List;

import com.authorization.life.system.app.dto.LsysLovValueDTO;
import com.authorization.life.system.app.vo.LsysLovValueVO;
import com.authorization.life.system.infra.entity.LsysLovValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 字典明细表 持久
 *
 * @author code@code.com 2024-10-05 19:21:43
 */
@Repository
public interface LsysLovValueMapper extends BaseMapper<LsysLovValue> {

    List<LsysLovValue> page(LsysLovValue lsysLovValue);

    List<LsysLovValueVO> listLovValueByParams(LsysLovValueDTO lovValueDTO);

}
