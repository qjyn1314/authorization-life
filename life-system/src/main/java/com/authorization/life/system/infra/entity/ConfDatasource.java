package com.authorization.life.system.infra.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据源表
 *
 * @author code@code.com
 * @date 2023-06-23 14:44:22
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@TableName("conf_datasource")
public class ConfDatasource implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_DATASOURCE_ID = "datasource_id";
    public static final String FIELD_SERVICE_NAME = "service_name";
    public static final String FIELD_DATASOURCE_NAME = "datasource_name";
    public static final String FIELD_DATABASE_NAME = "database_name";
    public static final String FIELD_DATABASE_IP = "database_ip";
    public static final String FIELD_DATABASE_PORT = "database_port";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_URL = "url";
    public static final String FIELD_CREATE_DATE = "create_date";
    public static final String FIELD_UPDATE_DATE = "update_date";
    public static final String FIELD_DEL_FLAG = "del_flag";

    /**
     * 主键
     */
    @TableId
    private Integer datasourceId;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * SPRING中动态数据源的名称
     */
    private String datasourceName;
    /**
     * 数据库名称
     */
    private String databaseName;
    /**
     * 数据库IP
     */
    private String databaseIp;
    /**
     * 数据库端口号
     */
    private String databasePort;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * jdbc_url
     */
    private String url;
    /**
     * 创建时间
     */
    private LocalDateTime createDate;
    /**
     * 更新
     */
    private LocalDateTime updateDate;
    /**
     * 删除标记
     */
    private String delFlag;

}
