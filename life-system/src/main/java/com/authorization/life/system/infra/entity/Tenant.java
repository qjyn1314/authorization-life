package com.authorization.life.system.infra.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
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
 * @date 2023-06-23 14:42:59
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@TableName("lemd_tenant")
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_TENANT_ID = "tenant_id";
    public static final String FIELD_TENANT_NAME = "tenant_name";
    public static final String FIELD_TENANT_CODE = "tenant_code";
    public static final String FIELD_CERTIFIED_FLAG = "certified_flag";
    public static final String FIELD_CERTIFIED_TIME = "certified_time";
    public static final String FIELD_COMPANY_ID = "company_id";
    public static final String FIELD_SUPPLIER_FLAG = "supplier_flag";
    public static final String FIELD_PURCHASE_FLAG = "purchase_flag";
    public static final String FIELD_VERSION_NUM = "version_num";
    public static final String FIELD_CREATED_BY_USER = "created_by_user";
    public static final String FIELD_CREATED_BY_EMP = "created_by_emp";
    public static final String FIELD_CREATED_TIME = "created_time";
    public static final String FIELD_UPDATED_BY_USER = "updated_by_user";
    public static final String FIELD_UPDATED_BY_EMP = "updated_by_emp";
    public static final String FIELD_UPDATED_TIME = "updated_time";

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
