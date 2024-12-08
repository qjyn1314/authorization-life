package com.authorization.life.lov.start.lov.entity;

import com.authorization.utils.contsant.BaseConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * 值集主信息
 */
@Setter
@Getter
public class LovDetail implements java.io.Serializable {

    public static class LovType {
        /**
         * 固定值集
         */
        public static final String FIXED = "FIXED";
    }

    /**
     * 值集代码
     */
    private String lovCode;
    /**
     * 值集名称
     */
    private String lovName;
    /**
     * 值集类型代码
     */
    private String lovTypeCode;
    /**
     * 租户ID
     */
    private String tenantId;

    public LovDetail() {
    }

    private LovDetail(String lovCode, String lovName) {
        this.lovCode = lovCode;
        this.lovName = lovName;
        this.lovTypeCode = LovType.FIXED;
        this.tenantId = BaseConstants.DEFAULT_TENANT_ID;
    }

    public static LovDetail of(String lovCode, String lovName) {
        return new LovDetail(lovCode, lovName);
    }

}
