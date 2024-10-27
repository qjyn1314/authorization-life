package com.authorization.life.auth.app.dto;

import com.authorization.valid.start.group.SaveGroup;
import com.authorization.valid.start.group.ValidGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@EqualsAndHashCode
public class LifeUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNum;
    private Integer pageSize;
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
    @Length(min = 5, max = 20, groups = {SaveGroup.class}, message = "请输入密码.")
    @Pattern(regexp = "^[\\W+|\\dA-Za-z]*", groups = {SaveGroup.class}, message = "密码必须是 字符, 大小写字母, 数字的组合, 请重新输入.")
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
    @Length(min = 5, max = 120, groups = {SaveGroup.class, ValidGroup.class}, message = "请输入正确的邮箱.")
    @Email(groups = {SaveGroup.class, ValidGroup.class}, message = "请输入正确的邮箱.")
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
     * redis中验证码的key
     */
    private String captchaUuid;

    /**
     * 验证码
     */
    @Length(min = 1, max = 10, groups = {SaveGroup.class}, message = "请输入验证码.")
    private String captchaCode;

}
