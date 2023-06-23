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
 * 员工表
 *
 * @author code@code.com
 * @date 2023-06-23 14:38:43
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@TableName("lemd_emp")
public class Emp implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_EMP_ID = "emp_id";
    public static final String FIELD_EMP_NUM = "emp_num";
    public static final String FIELD_EMP_NAME = "emp_name";
    public static final String FIELD_LOCALE = "locale";
    public static final String FIELD_TEL_AREA_CODE = "tel_area_code";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_BIRTHDAY = "birthday";
    public static final String FIELD_OFFICE_STATUS = "office_status";
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
    @TableId
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

}
