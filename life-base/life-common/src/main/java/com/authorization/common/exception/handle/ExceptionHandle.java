package com.authorization.common.exception.handle;

import com.authorization.common.exception.enums.ExceptionErrorEnums;
import com.authorization.utils.result.Result;
import com.authorization.valid.start.util.ValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 错误信息处理配置为所有异常以及编码编排不同的返回信息.
 *
 * @author wangjunming
 * @date 2023/1/4 13:48
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(Exception.class)
    public Result<Object> handleDefaultException(Exception defaultEx) {
        log.error("处理异常信息的通知内容->{}", defaultEx.getMessage());
        log.error("处理异常信息的通知->", defaultEx);
        if (defaultEx instanceof CommonException) {
            return Result.fail(defaultEx.getMessage());
        }
        if (defaultEx instanceof IllegalArgumentException) {
            return Result.fail(defaultEx.getMessage());
        }
        if (defaultEx instanceof ParamValidException) {
            return Result.fail(defaultEx.getMessage());
        }
        if (defaultEx instanceof NoResourceFoundException) {
            return Result.fail("未找到请求路径.");
        }
        if (defaultEx instanceof ValidException) {
            return Result.fail(defaultEx.getMessage());
        }
        if (defaultEx instanceof HttpRequestMethodNotSupportedException) {
            return Result.fail("请求方式不正确.");
        }
        return Result.fail(ExceptionErrorEnums.SYSTEM_EXCEPTION.formatMsg());
    }


}
