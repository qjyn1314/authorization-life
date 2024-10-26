package com.authorization.life.lov.start.lov.entity;

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
    private Long tenantId;


    private LovDetail(String lovCode, String lovName) {
        this.lovCode = lovCode;
        this.lovName = lovName;
        this.lovTypeCode = LovType.FIXED;
        this.tenantId = 0L;
    }

    public static LovDetail of(String lovCode, String lovName){
        return new LovDetail(lovCode,lovName);
    }

}
