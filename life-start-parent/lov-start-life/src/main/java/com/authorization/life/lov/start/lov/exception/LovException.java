package com.authorization.life.lov.start.lov.exception;


/**
 * 翻译异常
 */
public class LovException extends RuntimeException {

    private static final String MODULE = "Lov Exception";

    private static final String DEFAULT_MESSAGE = "lov异常";

    public LovException() {
        super(DEFAULT_MESSAGE);
    }

    public LovException(String message) {
        super(message);
    }

}
