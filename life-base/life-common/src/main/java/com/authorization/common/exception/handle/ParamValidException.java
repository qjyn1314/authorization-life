package com.authorization.common.exception.handle;

import cn.hutool.core.text.CharSequenceUtil;
import com.authorization.common.exception.enums.ExceptionErrorCode;

/**
 *
 * 参数校验异常, 异常提示可以拼接数据, 例如: 异常信息: 你好:{},张三 -> 最终返回的:你好:张三
 *
 * @author wangjunming
 * @date 2025/3/12 16:41
 */
public class ParamValidException extends CommonException {
    private final int code;

    public ParamValidException(ExceptionErrorCode resultEnum, Object... param) {
        super(
                param.length == 0
                        ? resultEnum.formatMsg()
                        : CharSequenceUtil.format(resultEnum.formatMsg(), param));
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return this.code;
    }
}
