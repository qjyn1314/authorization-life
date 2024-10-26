package com.authorization.life.lov.start.lov.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 值集子表
 */
@Setter
@Getter
public class LovValueDetail implements java.io.Serializable {

    /**
     * 值集代码
     */
    private String lovCode;
    /**
     * 值代码
     */
    private String valueCode;
    /**
     * 值内容
     */
    private String valueContent;
    /**
     * 租户ID
     */
    private Long tenantId;

    private LovValueDetail(String lovCode, String valueCode, String valueContent) {
        this.lovCode = lovCode;
        this.valueCode = valueCode;
        this.valueContent = valueContent;
        this.tenantId = 0L;
    }

    public static LovValueDetail of(String lovCode, String valueCode, String valueContent) {
        return new LovValueDetail(lovCode, valueCode, valueContent);
    }

}
