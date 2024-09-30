package com.authorization.life.auth.infra.entity;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.authorization.mybatis.start.entity.AuditEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * 用户表
 *
 * @author code@code.com
 * @date 2022-02-21 20:23:16
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("liam_user")
public class LifeUser extends AuditEntity implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_REAL_NAME = "realName";
    public static final String FIELD_LANG = "lang";
    public static final String FIELD_LOCALE = "locale";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_HASH_PASSWORD = "hashPassword";
    public static final String FIELD_TEL_AREA_CODE = "telAreaCode";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_PHONE_CHECKED_FLAG = "phoneCheckedFlag";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_EMAIL_CHECKED_FLAG = "emailCheckedFlag";
    public static final String FIELD_BIRTHDAY = "birthday";
    public static final String FIELD_EFFECTIVE_START_DATE = "effectiveStartDate";
    public static final String FIELD_EFFECTIVE_END_DATE = "effectiveEndDate";
    public static final String FIELD_ACTIVED_FLAG = "activedFlag";
    public static final String FIELD_LOCKED_FLAG = "lockedFlag";
    public static final String FIELD_LOCKED_TIME = "lockedTime";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_VERSION_NUM = "versionNum";

    /**
     * 用户表主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String userId;
    /**
     * 用户编码
     */
    private String username;
    /**
     * 用户姓名
     */
    private String realName;
    /**
     * 语言,默认是中文
     */
    private String lang;
    /**
     * 地区,默认是中国
     */
    private String locale;
    /**
     * 用户性别:1:男 2:女 3：未知
     */
    private Integer gender;
    /**
     * 密码
     */
    private String hashPassword;
    /**
     * 电话区号。
     */
    private String telAreaCode;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 手机号通过验证标识
     */
    private Boolean phoneCheckedFlag;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 邮箱通过验证标识
     */
    private Boolean emailCheckedFlag;
    /**
     * 出生日期
     */
    private LocalDateTime birthday;
    /**
     * 生效开始日期
     */
    private LocalDateTime effectiveStartDate;
    /**
     * 生效截至日期
     */
    private LocalDateTime effectiveEndDate;
    /**
     * 已激活标识
     */
    private Boolean activedFlag;
    /**
     * 已锁定标识
     */
    private Boolean lockedFlag;
    /**
     * 锁定时间
     */
    private LocalDateTime lockedTime;
    /**
     * 状态：1-启用；0-未启用
     */
    private Boolean enabledFlag;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 版本号
     */
    private Long versionNum;

    /**
     * 用户组信息
     */
    @TableField(exist = false)
    private Set<String> userGroups;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.hashPassword;
    }

    @Override
    public String getUsername() {
        return StrUtil.isNotBlank(username) ? username.trim() : username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return Objects.isNull(this.effectiveEndDate) || LocalDateTime.now().isBefore(this.effectiveEndDate);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !(BooleanUtil.isTrue(this.lockedFlag) && (Objects.isNull(lockedTime) || LocalDateTime.now().isBefore(this.lockedTime)));
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return BooleanUtil.isTrue(this.enabledFlag);
    }


}
