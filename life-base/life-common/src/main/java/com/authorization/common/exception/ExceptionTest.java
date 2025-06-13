package com.authorization.common.exception;

import com.authorization.common.exception.enums.SecurityErrorEnums;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2025-06-13 17:53
 */
@Slf4j
public class ExceptionTest {


    public static void main(String[] args) {


        log.info("SecurityErrorEnums.LOGIN_IS_INVALID.formatMsg()--{}", SecurityErrorEnums.LOGIN_IS_INVALID.formatMsg());


    }

}
