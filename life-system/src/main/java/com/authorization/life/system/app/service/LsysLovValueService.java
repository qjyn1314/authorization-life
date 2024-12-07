package com.authorization.life.system.app.service;

import com.authorization.life.system.app.dto.LsysLovValueDTO;
import com.authorization.life.system.app.vo.LsysLovValueVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 字典明细表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:43
 */
public interface LsysLovValueService {


    PageInfo<LsysLovValueVO> page(LsysLovValueDTO lovValueDTO);


    List<LsysLovValueVO> listByParams(LsysLovValueDTO lovValueDTO);


    String saveLovValue(LsysLovValueDTO lovValueDTO);


    LsysLovValueVO lovValueByParams(LsysLovValueDTO lovValueDTO);


    String updateLovValue(LsysLovValueDTO lovValueDTO);


    String deleteLovValue(LsysLovValueDTO lovValueDTO);


    PageInfo<LsysLovValueVO> lovValuePage(LsysLovValueDTO lovValueDTO);

    List<LsysLovValueVO> listLovValueByParams(LsysLovValueDTO lovValueDTO);

    String batchDeleteLovValue(List<String> lovValueIds);

}
