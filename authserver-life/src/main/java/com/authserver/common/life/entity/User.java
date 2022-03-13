package com.authserver.common.life.entity;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 用户表
 *
 * @author code@code.com
 * @date 2022-02-21 20:23:16
 */
@Data
@TableName("lifetime_user")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表主键
     */
    @TableId
    private Long userId;
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
    private Boolean gender;
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
    private Long tenantId;
    /**
     * 版本号
     */
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

    /**用户组信息*/
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
