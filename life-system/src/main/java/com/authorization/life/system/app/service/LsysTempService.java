package com.authorization.life.system.app.service;

import com.authorization.life.system.app.dto.LsysTempDTO;
import com.authorization.life.system.app.vo.LsysTempVO;
import com.authorization.remote.system.vo.TempVO;
import com.github.pagehelper.PageInfo;

/**
 * 模版
 *
 * @author code@code.com
 * @date 2024-10-01 23:07:37
 */
public interface LsysTempService {


    TempVO getTempByCode(String code);

    String saveTemp(LsysTempDTO sysTemp);

    PageInfo<LsysTempVO> pageTemp(LsysTempDTO sysTemp);

    LsysTempVO tempByParams(LsysTempDTO sysTemp);

    String updateTemp(LsysTempDTO sysTemp);

    String deleteTemp(LsysTempDTO sysTemp);

}
