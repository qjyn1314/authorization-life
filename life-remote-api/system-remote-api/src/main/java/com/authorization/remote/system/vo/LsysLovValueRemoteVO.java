package com.authorization.remote.system.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 字典明细表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:43
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class LsysLovValueRemoteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_LOV_VALUE_ID = "lov_value_id";
    public static final String FIELD_LOV_ID = "lov_id";
    public static final String FIELD_LOV_CODE = "lov_code";
    public static final String FIELD_VALUE_CODE = "value_code";
    public static final String FIELD_VALUE_CONTENT = "value_content";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_VALUE_ORDER = "value_order";
    public static final String FIELD_ENABLED_FLAG = "enabled_flag";
    public static final String FIELD_VERSION_NUM = "version_num";

    /**
     * 固定值集主键
     */
    private String lovValueId;
    /**
     * 值集表主键, ztnt_lov.lovid
     */
    private String lovId;
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
     * 描述
     */
    private String description;
    /**
     * 排序号
     */
    private Integer valueOrder;
    /**
     * 生效标识：1:生效，0:失效
     */
    private Boolean enabledFlag;
    /**
     * 版本号
     */
    private Long versionNum;
    /**
     * 租户ID
     */
    private String tenantId;

}
