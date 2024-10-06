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
@TableName("lsys_lov")
public class LsysLov extends AuditEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_LOV_ID = "lov_id";
    public static final String FIELD_LOV_CODE = "lov_code";
    public static final String FIELD_LOV_TYPE_CODE = "lov_type_code";
    public static final String FIELD_LOV_NAME = "lov_name";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ENABLED_FLAG = "enabled_flag";
    public static final String FIELD_TENANT_ID = "tenant_id";
    public static final String FIELD_VERSION_NUM = "version_num";

    /**
     * 值集主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String lovId;
    /**
     * 值集代码
     */
    private String lovCode;
    /**
     * LOV类型：FIXED/URL
     */
    private String lovTypeCode;
    /**
     * 值集名称
     */
    private String lovName;
    /**
     * 描述
     */
    private String description;
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
