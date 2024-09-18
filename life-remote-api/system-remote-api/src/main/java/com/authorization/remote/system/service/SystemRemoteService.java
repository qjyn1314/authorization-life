package com.authorization.remote.system.service;

import com.authorization.remote.system.SystemRemoteRes;

/**
 * System-life 服务 暴露的远程接口
 *
 * @author wangjunming
 * @date 2022/12/23 14:17
 */
public interface SystemRemoteService {


    SystemRemoteRes<String> getSystemRemoteNowDate();

}
