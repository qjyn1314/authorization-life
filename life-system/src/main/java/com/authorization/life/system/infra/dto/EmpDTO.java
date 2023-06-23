package com.authorization.life.system.infra.dto;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工表
 *
 * @author code@code.com
 * @date 2023-04-24 14:08:47
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class EmpDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long empId;
    /**
     * 员工编号
     */
    private String empNum;
    /**
     * 员工名称
     */
    private String empName;
    /**
     * 地区,默认是中国
     */
    private String locale;
    /**
     * 区号：默认+86
     */
    private String telAreaCode;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户性别:1:男 2:女 3：未知
     */
    private Boolean gender;
    /**
     * 出生日期
     */
    private LocalDateTime birthday;
    /**
     * 任职状态：实习，试用，在职，交接，离职
     */
    private String officeStatus;
    /**
     * 启用：true（1）；禁用：false（0）
     */
    private Boolean enabledFlag;
    /**
     * 租户ID
     */
    private Long tenantId;
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

    private int pageNum;

    private int pageSize;

}
