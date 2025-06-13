package com.authorization.common.exception;

import com.authorization.common.exception.handle.ExceptionHandle;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 配置异常处理类通知
 *
 * @author wangjunming
 * @date 2023/3/13 14:31
 */
@AutoConfiguration
public class ExceptionConfig {

    @Bean
    public ExceptionHandle exceptionHandle() {
        return new ExceptionHandle();
    }

}
