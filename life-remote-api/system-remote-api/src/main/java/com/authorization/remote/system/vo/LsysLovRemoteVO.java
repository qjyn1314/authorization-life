package com.authorization.remote.system.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 字典主表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:32
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class LsysLovRemoteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 值集主键
     */
    private String lovId;
    /**
     * 值集代码
     */
    private String lovCode;
    /**
     * LOV类型：FIXED/URL
     */
    private String lovTypeCode;
    /**
     * 值集名称
     */
    private String lovName;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否启用
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
