package com.authorization.gateway.execption;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.authorization.gateway.entity.Result;
import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;

/**
 * 错误信息处理类
 */
@Slf4j
public class ReactiveExceptionHandler extends DefaultErrorWebExceptionHandler {

    private static final String FIELD_HTTP_STATUS = "httpStatus";

    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param webProperties      the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     */
    public ReactiveExceptionHandler(ErrorAttributes errorAttributes, WebProperties webProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, webProperties.getResources(), errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        // 默认502异常
        int httpStatus = HttpStatus.BAD_GATEWAY.value();
        // 未知错误，请联系管理员
        Result result = new Result(Result.ERROR, "Api gateway unknown error, Please contact the administrator");
        Throwable e = super.getError(request);
        log.error(exceptionFormat("Gateway Exception", request), e);
        if (e instanceof ConnectTimeoutException) {
            // 连接超时
            httpStatus = HttpStatus.GATEWAY_TIMEOUT.value();
            result = new Result(Result.ERROR, "The target service instance connect timeout");
        } else if (e instanceof UnknownHostException) {
            // 找不到主机
            httpStatus = HttpStatus.BAD_GATEWAY.value();
            result = new Result(Result.ERROR, "The target service [" + e.getMessage() + "] not found");
        } else if (e instanceof ResponseStatusException && Objects.equals(((ResponseStatusException) e).getStatus(), HttpStatus.GATEWAY_TIMEOUT)) {
            // 响应超时
            ResponseStatusException exception = (ResponseStatusException) e;
            httpStatus = exception.getStatus().value();
            result = new Result(Result.ERROR, "The target service instance response timeout, Pleade retry on later");
        } else if (e instanceof ResponseStatusException) {
            // 其他异常
            ResponseStatusException exception = (ResponseStatusException) e;
            httpStatus = exception.getStatus().value();
            result = new Result(Result.ERROR, exception.getReason());
        }
        Map<String, Object> map = BeanUtil.beanToMap(result, false, true);
        map.put(FIELD_HTTP_STATUS, httpStatus);
        return map;
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes 异常属性
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 根据code获取对应的HttpStatus
     *
     * @param errorAttributes 异常属性
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        int httpStatus = (int) errorAttributes.get(FIELD_HTTP_STATUS);
        errorAttributes.remove(FIELD_HTTP_STATUS);
        return httpStatus;
    }

    /**
     * 异常基础格式
     *
     * @param message 异常消息
     * @param request request
     * @return 格式化后的异常消息
     */
    protected String exceptionFormat(String message, ServerRequest request) {
        return StrUtil.format("{}, Request: [URI={}]", message, request.exchange().getRequest().getPath());
    }
}
