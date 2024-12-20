package com.authorization.life.auth.infra.entity;

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
 * 用户组表
 *
 * @author code@code.com
 * @date 2022-02-21 20:24:00
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("liam_user_group")
public class UserGroup extends AuditEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_USER_GROUP_ID = "userGroupId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_USER_GROUP_CODE = "userGroupCode";

    /**
     * 用户组表主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String userGroupId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户组编码, 与SCOPE编码保持一致
     */
    private String userGroupCode;


}
