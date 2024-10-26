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
 * 字典主表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:32
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LsysLovDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer pageNum;
    private Integer pageSize;
    /**
     * 值集主键
     */
    @Length(min = 1, max = 120, message = "主键Id不能为空.", groups = {UpdateGroup.class})
    private String lovId;
    /**
     * 值集代码
     */
    @Length(min = 1, max = 120, message = "值集编码不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String lovCode;
    /**
     * LOV类型：FIXED/URL
     */
    @Length(min = 1, max = 120, message = "值集类型不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String lovTypeCode;
    /**
     * 值集名称
     */
    @Length(min = 1, max = 120, message = "值集名称不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String lovName;
    /**
     * 描述
     */
    @Length(min = 1, max = 120, message = "值集描述不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String description;
    /**
     * 是否启用
     */
    @NotNull(message = "请选择是否启用.", groups = {SaveGroup.class, UpdateGroup.class})
    private Boolean enabledFlag;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 版本号
     */
    private Long versionNum;

}
