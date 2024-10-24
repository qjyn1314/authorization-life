package com.authorization.life.system.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

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
public class LsysLovDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer pageNum;
    private Integer pageSize;
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
     * 租户ID
     */
    private String tenantId;
    /**
     * 版本号
     */
    private Long versionNum;

}
