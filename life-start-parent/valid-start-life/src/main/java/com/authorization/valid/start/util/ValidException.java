package com.authorization.valid.start.util;

/**
 * 校验异常
 *
 * @author wangjunming
 */
public class ValidException extends RuntimeException {

    private static final String MODULE = "Valid Exception";

    private static final String DEFAULT_MESSAGE = "校验异常";

    public ValidException(String code) {
        super(DEFAULT_MESSAGE);
    }

}
