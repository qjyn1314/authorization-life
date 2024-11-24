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
 * 模版
 *
 * @author code@code.com
 * @date 2024-10-01 23:07:37
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LsysTempDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer pageNo;
    private Integer pageSize;
    /**
     * 模版ID
     */
    @Length(min = 1, max = 80, message = "主键Id不能为空.", groups = {UpdateGroup.class})
    private String tempId;
    /**
     * 编码
     */
    @Length(min = 1, max = 80, message = "模板编码不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String tempCode;
    /**
     * 模版描述
     */
    @Length(min = 1, max = 120, message = "模板描述不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String tempDesc;
    /**
     * 模板类型
     */
    @Length(min = 1, max = 30, message = "模板类型不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String tempType;
    /**
     * 模版内容
     */
    @Length(min = 1, message = "模板内容不能为空.", groups = {SaveGroup.class, UpdateGroup.class})
    private String content;
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
