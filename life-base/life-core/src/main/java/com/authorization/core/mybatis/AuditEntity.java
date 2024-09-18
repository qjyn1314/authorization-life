package com.authorization.core.mybatis;

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

    /**
     * 创建用户
     */
    private String createdByUser;
    /**
     * 创建员工
     */
    private String createdByEmp;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 最后更新用户
     */
    private String updatedByUser;
    /**
     * 最后更新员工
     */
    private String updatedByEmp;
    /**
     * 最后更新时间
     */
    private LocalDateTime updatedTime;

}
