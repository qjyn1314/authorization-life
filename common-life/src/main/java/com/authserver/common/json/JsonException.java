package com.authserver.common.json;


/**
 * Json异常
 */
public class JsonException extends RuntimeException {

    private static final String MODULE = "Json Exception";

    public JsonException(Throwable cause) {
        super(MODULE, cause);
    }
}
