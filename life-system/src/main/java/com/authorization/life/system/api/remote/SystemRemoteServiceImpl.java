package com.authorization.life.system.api.remote;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.authorization.common.log.LogAdvice;
import com.authorization.remote.system.service.SystemRemoteService;
import com.authorization.utils.result.R;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * System-life 服务 暴露的远程接口实现
 *
 * @author wangjunming
 * @date 2022/12/23 14:16
 */
@Service
public class SystemRemoteServiceImpl implements SystemRemoteService {

    @LogAdvice(name = "获取远程接口中的当前时间")
    @Override
    public R<String> getSystemRemoteNowDate() {
        return R.ok(DateUtil.format(LocalDateTime.now(), DatePattern.CHINESE_DATE_TIME_PATTERN));
    }

}
