package com.authorization.life.system.infra.entity;

import com.authorization.mybatis.start.entity.AuditEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 模板接收人员
 *
 * @author code@code.com
 * @date 2024-10-01 23:08:40
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("lsys_temp_user")
public class LsysTempUser extends AuditEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_TEMP_USER_ID = "temp_user_id";
    public static final String FIELD_TEMP_CODE = "temp_code";
    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_ENABLED_FLAG = "enabled_flag";
    public static final String FIELD_TENANT_ID = "tenant_id";
    public static final String FIELD_VERSION_NUM = "version_num";
    public static final String FIELD_CREATED_BY_USER = "created_by_user";
    public static final String FIELD_CREATED_BY_EMP = "created_by_emp";
    public static final String FIELD_CREATED_TIME = "created_time";
    public static final String FIELD_UPDATED_BY_USER = "updated_by_user";
    public static final String FIELD_UPDATED_BY_EMP = "updated_by_emp";
    public static final String FIELD_UPDATED_TIME = "updated_time";

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String tempUserId;
    /**
     * 模板编码
     */
    private String tempCode;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户编码
     */
    private String username;
    /**
     * 状态：1-启用；0-未启用
     */
    private Integer enabledFlag;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 版本号
     */
    private Long versionNum;

}
