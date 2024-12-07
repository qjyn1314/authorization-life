package com.authorization.life.system.app.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LsysLovValueVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 固定值集主键
     */
    private String lovValueId;
    /**
     * 值集表主键, ztnt_lov.lovid
     */
    private String lovId;
    /**
     * 值集名称
     */
    private String lovName;
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
     * 租户ID
     */
    private String tenantId;
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
     * 创建时间
     */
    private LocalDateTime createdTime;

}
