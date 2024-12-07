package com.authorization.life.system.app.service;

import com.authorization.life.lov.start.lov.entity.LovValueDetail;
import com.authorization.life.system.app.dto.LsysLovDTO;
import com.authorization.life.system.app.vo.LsysLovVO;
import com.authorization.remote.system.vo.LsysLovRemoteVO;
import com.authorization.remote.system.vo.LsysLovValueRemoteVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 字典主表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:32
 */
public interface LsysLovService {


    PageInfo<LsysLovVO> page(LsysLovDTO lovDTO);


    List<LsysLovVO> listByParams(LsysLovDTO lovDTO);


    String saveLov(LsysLovDTO sysLov);


    LsysLovVO lovByParams(LsysLovDTO sysLov);


    String updateLov(LsysLovDTO sysLov);


    String deleteLov(LsysLovDTO sysLov);


    LsysLovRemoteVO lovCacheAndDataSource(String tenantId, String lovCode);


    List<LsysLovValueRemoteVO> lovvalueCacheAndDataSource(String tenantId, String lovCode);

    String deleteLovByIds(List<String> lovIds);

}
