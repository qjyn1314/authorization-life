package com.authorization.life.auth.infra.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户组表
 *
 * @author code@code.com
 * @date 2022-02-21 20:24:00
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("lifetime_user_group")
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_USER_GROUP_ID = "userGroupId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_USER_GROUP_CODE = "userGroupCode";
    public static final String FIELD_TENANT_ID = "tenantId";

    /**
     * 用户组表主键
     */
    @TableId
    private Long userGroupId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户组编码, 与SCOPE编码保持一致
     */
    private String userGroupCode;
    /**
     * 租户ID
     */
    private Long tenantId;
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
