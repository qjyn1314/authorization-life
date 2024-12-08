package com.authorization.life.system.app.vo;

import com.authorization.life.lov.start.lov.anno.LovValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LsysLovVO implements Serializable {

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
    @LovValue(lovCode = "SYS_LOV_TYPE")
    private String lovTypeCode;
    /**
     * LOV类型：FIXED/URL
     */
    private String lovTypeCodeContent;
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
    @LovValue(lovCode = "FLAG_STATUS")
    private Boolean enabledFlag;
    /**
     * 是否启用
     */
    private String enabledFlagContent;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 版本号
     */
    private Long versionNum;

     /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}
