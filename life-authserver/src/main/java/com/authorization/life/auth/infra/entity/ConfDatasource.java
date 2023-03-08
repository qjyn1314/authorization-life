package com.authorization.life.auth.infra.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据源表
 *
 * @author code@code.com
 * @date 2023-02-24 13:29:22
 */
@Data
@TableName("conf_datasource")
public class ConfDatasource implements Serializable {

    public static final String FIELD_ID = "id";
    public static final String FIELD_DATASOURCE_NAME = "datasourceName";
    public static final String FIELD_DATABASE_NAME = "databaseName";
    public static final String FIELD_DATABASE_IP = "databaseIp";
    public static final String FIELD_DATABASE_PORT = "databasePort";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_URL = "url";
    public static final String FIELD_CREATE_DATE = "createDate";
    public static final String FIELD_UPDATE_DATE = "updateDate";
    public static final String FIELD_DEL_FLAG = "delFlag";


    /**
     * 主键
     */
    @TableId
    private Integer id;
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