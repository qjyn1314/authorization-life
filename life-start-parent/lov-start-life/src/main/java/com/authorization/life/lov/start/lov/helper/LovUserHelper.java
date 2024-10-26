package com.authorization.life.lov.start.lov.helper;

/**
 * lov当前使用者
 */
public interface LovUserHelper {

    /**
     * @return 获取当前租户Id
     */
    default Long getTenantId(){
        return 0L;
    }
}
