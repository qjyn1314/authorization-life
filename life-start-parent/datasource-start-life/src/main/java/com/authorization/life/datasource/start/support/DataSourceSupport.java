package com.authorization.life.datasource.start.support;

/**
 * <p>
 * 数据源相关常量
 */
public interface DataSourceSupport {


    /**
     * 默认数据源（master）-即当前应用配置的默认数据源
     */
    String DS_MASTER = "master";

    /**
     * 数据源名称
     */
    String DS_NAME = "datasource_name";

    /**
     * databaseip
     */
    String DATABASE_IP = "database_ip";

    /**
     * database_port
     */
    String DATABASE_PORT = "database_port";

    /**
     * database_name
     */
    String DATABASE_NAME = "database_name";

    /**
     * format_url
     */
    String FORMAT_URL = "url";

    /**
     * 用户名
     */
    String DS_USER_NAME = "username";

    /**
     * 密码
     */
    String DS_USER_PWD = "password";

    /**
     * 驱动包名称
     */
    String DS_DRIVER_CLASS_NAME = "driver_class_name";

}
