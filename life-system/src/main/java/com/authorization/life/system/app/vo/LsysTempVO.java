package com.authorization.life.system.app.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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
public class LsysTempVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模版ID
     */
    private String tempId;
    /**
     * 编码
     */
    private String tempCode;
    /**
     * 模版描述
     */
    private String tempDesc;
    /**
     * 模板类型
     */
    private String tempType;
    /**
     * 模版内容
     */
    private String content;
    /**
     * 是否启用
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

}
