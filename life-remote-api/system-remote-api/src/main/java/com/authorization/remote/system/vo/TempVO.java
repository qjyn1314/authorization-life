package com.authorization.remote.system.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 模板数据
 * </p>
 *
 * @author wangjunming
 * @since 2024-10-01 23:18
 */
@Setter
@Getter
public class TempVO implements Serializable {

    /**
     * 模版ID
     */
    private String tempId;
    /**
     * 编码
     */
    private String tempCode;
    /**
     * 模版描述
     */
    private String tempDesc;
    /**
     * 模板类型
     */
    private String tempType;
    /**
     * 模版内容
     */
    private String content;
    /**
     * 是否启用
     */
    private Integer enabledFlag;
    /**
     * 租户ID
     */
    private String tenantId;

}
