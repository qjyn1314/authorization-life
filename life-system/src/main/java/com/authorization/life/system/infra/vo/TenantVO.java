package com.authorization.life.system.infra.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户信息表
 *
 * @author code@code.com
 * @date 2023-04-11 11:14:24
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class TenantVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 租户编码
     */
    private String tenantCode;
    /**
     * 认证：true（1）；没认证：false（0）
     */
    private Boolean certifiedFlag;
    /**
     * 认证时间
     */
    private LocalDateTime certifiedTime;
    /**
     * 公司id
     */
    private Long companyId;
    /**
     * 供应商标识
     */
    private Boolean supplierFlag;
    /**
     * 采购商标识
     */
    private Boolean purchaseFlag;
    /**
     * 版本号
     */
    private Long versionNum;
    /**
     * 创建用户
     */
    private Long createdByUser;
    /**
     * 创建员工
     */
    private Long createdByEmp;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 最后更新用户
     */
    private Long updatedByUser;
    /**
     * 最后更新员工
     */
    private Long updatedByEmp;
    /**
     * 最后更新时间
     */
    private LocalDateTime updatedTime;

}
