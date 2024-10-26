package com.authorization.life.system.app.dto;

import com.authorization.valid.start.group.SaveGroup;
import com.authorization.valid.start.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 字典明细表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:43
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LsysLovValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer pageNum;
    private Integer pageSize;
    /**
     * 固定值集主键
     */
    @Length(min = 1, max = 120, message = "主键Id不能为空.", groups = {UpdateGroup.class})
    private String lovValueId;
    /**
     * 值集表主键, ztnt_lov.lovid
     */
    @Length(min = 1, max = 120, message = "值集编码不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String lovId;
    /**
     * 值集代码
     */
    private String lovCode;
    /**
     * 值代码
     */
    @Length(min = 1, max = 120, message = "值代码不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String valueCode;
    /**
     * 值内容
     */
    @Length(min = 1, max = 120, message = "值内容不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String valueContent;
    /**
     * 描述
     */
    private String description;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 排序号
     */
    @NotNull(message = "排序不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private Integer valueOrder;
    /**
     * 生效标识：1:生效，0:失效
     */
    @NotNull(message = "请选择是否生效.", groups = {SaveGroup.class, UpdateGroup.class})
    private Boolean enabledFlag;
    /**
     * 版本号
     */
    private Long versionNum;

}
