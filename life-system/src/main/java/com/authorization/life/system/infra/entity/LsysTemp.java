package com.authorization.life.system.infra.entity;

import com.authorization.mybatis.start.entity.AuditEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

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
@TableName("lsys_temp")
public class LsysTemp extends AuditEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_TEMP_ID = "temp_id";
    public static final String FIELD_TEMP_CODE = "temp_code";
    public static final String FIELD_TEMP_DESC = "temp_desc";
    public static final String FIELD_TEMP_TYPE = "temp_type";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_ENABLED_FLAG = "enabled_flag";
    public static final String FIELD_VERSION_NUM = "version_num";

    /**
     * 模版ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
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
     * 版本号
     */
    private Long versionNum;

}
