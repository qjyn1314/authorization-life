package com.authorization.remote.system.service;

import com.authorization.remote.system.SystemRemoteRes;
import com.authorization.remote.system.vo.LsysLovRemoteVO;
import com.authorization.remote.system.vo.LsysLovValueRemoteVO;
import com.authorization.remote.system.vo.TempVO;

import java.util.List;

/**
 * System-life 服务 暴露的远程接口
 *
 * @author wangjunming
 * @date 2022/12/23 14:17
 */
public interface SystemRemoteService {


    SystemRemoteRes<String> getSystemRemoteNowDate();

    SystemRemoteRes<TempVO> getTempByCode(String code);

    SystemRemoteRes<LsysLovRemoteVO> selectLov(String tenantId, String lovCode);

    SystemRemoteRes<List<LsysLovValueRemoteVO>> selectLovValue(String tenantId, String lovCode);

}
