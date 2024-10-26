package com.authorization.life.lov.start.lov.helper;

import com.authorization.utils.contsant.BaseConstants;

/**
 * lov当前使用者
 */
public interface LovUserHelper {

    /**
     * @return 获取当前租户Id
     */
    default String getTenantId() {
        return BaseConstants.DEFAULT_TENANT_ID;
    }
}
