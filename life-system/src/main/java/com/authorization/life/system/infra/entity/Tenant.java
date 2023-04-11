package com.authorization.life.system.infra.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serializable;

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
@TableName("life_tenant")
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TENANT_NAME = "tenantName";
    public static final String FIELD_TENANT_CODE = "tenantCode";
    public static final String FIELD_CERTIFIED_FLAG = "certifiedFlag";
    public static final String FIELD_CERTIFIED_TIME = "certifiedTime";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_SUPPLIER_FLAG = "supplierFlag";
    public static final String FIELD_PURCHASE_FLAG = "purchaseFlag";
    public static final String FIELD_VERSION_NUM = "versionNum";
    public static final String FIELD_CREATED_BY_USER = "createdByUser";
    public static final String FIELD_CREATED_BY_EMP = "createdByEmp";
    public static final String FIELD_CREATED_TIME = "createdTime";
    public static final String FIELD_UPDATED_BY_USER = "updatedByUser";
    public static final String FIELD_UPDATED_BY_EMP = "updatedByEmp";
    public static final String FIELD_UPDATED_TIME = "updatedTime";

    /**
     * 租户id
     */
    @TableId
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
    @Version
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
