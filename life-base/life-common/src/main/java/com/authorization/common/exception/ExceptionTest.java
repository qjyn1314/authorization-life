package com.authorization.common.exception;

import com.authorization.common.exception.enums.SecurityErrorEnums;
import com.authorization.utils.security.UserDetail;
import com.authorization.utils.security.UserDetailUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangjunming
 * @since 2025-06-13 17:53
 */
@Slf4j
public class ExceptionTest {

  public static void main(String[] args) {

    UserDetailUtil.setUserContext(UserDetail.anonymous());

    log.info(
        "SecurityErrorEnums.LOGIN_IS_INVALID.formatMsg()--{}",
        SecurityErrorEnums.LOGIN_IS_INVALID.formatMsg());

    UserDetailUtil.remove();
  }
}
