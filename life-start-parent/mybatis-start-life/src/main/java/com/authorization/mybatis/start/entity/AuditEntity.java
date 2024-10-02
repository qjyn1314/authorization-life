package com.authorization.mybatis.start.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统中的默认字段, 即审计字段
 * </p>
 *
 * @author wangjunming
 * @since 2024-09-18 10:23
 */
@Setter
@Getter
@EqualsAndHashCode
public class AuditEntity implements Serializable {

    public static final String FIELD_CREATED_BY_USER = "createdByUser";
    public static final String FIELD_CREATED_BY_EMP = "createdByEmp";
    public static final String FIELD_CREATED_TIME = "createdTime";
    public static final String FIELD_UPDATED_BY_USER = "updatedByUser";
    public static final String FIELD_UPDATED_BY_EMP = "updatedByEmp";
    public static final String FIELD_UPDATED_TIME = "updatedTime";


    /**
     * 创建用户
     */
    @TableField(fill = FieldFill.INSERT)
    private String createdByUser;
    /**
     * 创建员工
     */
    @TableField(fill = FieldFill.INSERT)
    private String createdByEmp;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    /**
     * 最后更新用户
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedByUser;
    /**
     * 最后更新员工
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedByEmp;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

}
