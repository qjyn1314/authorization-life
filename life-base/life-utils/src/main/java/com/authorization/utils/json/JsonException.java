package com.authorization.utils.json;

/**
 * Json异常
 */
public class JsonException extends RuntimeException {

    private static final String MODULE = "Json Exception";

    private static final String DEFAULT_MESSAGE = "Json通用异常";

    public JsonException(Throwable ex) {
        super(MODULE, ex);
    }

}
