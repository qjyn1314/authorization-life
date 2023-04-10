package com.authorization.life.datasource.start.config;

import com.authorization.life.datasource.start.support.DataSourceSupport;
import com.authorization.utils.kvp.KvpFormat;
import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 从数据源中获取 配置信息
 */
@Slf4j
public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

    private final DataSourceProperties properties;

    private final StringEncryptor stringEncryptor;

    public JdbcDynamicDataSourceProvider(StringEncryptor stringEncryptor, DataSourceProperties properties) {
        super(properties.getDriverClassName(), properties.getUrl(), properties.getUsername(), properties.getPassword());
        this.stringEncryptor = stringEncryptor;
        this.properties = properties;
    }

    /**
     * 执行语句获得数据源参数
     *
     * @param statement 语句
     * @return 数据源参数
     * @throws SQLException sql异常
     */
    @Override
    protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
        log.info("加载JDBC动态数据源...");
        Map<String, DataSourceProperty> map = new ConcurrentHashMap<>(8);
        // 添加默认主数据源, 即 工程的配置文件中配置的数据源为主数据源
        DataSourceProperty property = new DataSourceProperty();
        property.setUrl(properties.getUrl());
        property.setUsername(properties.getUsername());
        property.setPassword(properties.getPassword());
        property.setDriverClassName(properties.getDriverClassName());
        property.setType(properties.getType());
        map.put(DataSourceSupport.DS_MASTER, property);

        ResultSet rs = null;
        try {
            // 从数据库中查询多数据源表获取数据源
            rs = statement.executeQuery(properties.getQueryDsSql());
        } catch (SQLException e) {
            log.error("查询从数据源失败...{}", e.getMessage());
        }
        if (Objects.nonNull(rs)) {
            setSalveDataSource(rs, map);
        }
        return map;
    }

    /**
     * 设置从数据库
     *
     * @param rs  数据库查询数据源结果
     * @param map 数据源map信息
     * @throws SQLException
     */
    private void setSalveDataSource(ResultSet rs, Map<String, DataSourceProperty> map) throws SQLException {
        while (rs.next()) {
            String name = rs.getString(DataSourceSupport.DS_NAME);
            String username = rs.getString(DataSourceSupport.DS_USER_NAME);
            String password = rs.getString(DataSourceSupport.DS_USER_PWD);
            log.info("数据库中的密码->{}", password);
            password = stringEncryptor.decrypt(password);
            log.info("解析数据库中的密码成功后->{}", password);
            String databaseIp = rs.getString(DataSourceSupport.DATABASE_IP);
            String databasePort = rs.getString(DataSourceSupport.DATABASE_PORT);
            String databaseName = rs.getString(DataSourceSupport.DATABASE_NAME);
            String url = rs.getString(DataSourceSupport.FORMAT_URL);
            String realJdbcUrl = KvpFormat.of(url).add(DataSourceSupport.DATABASE_IP, databaseIp)
                    .add(DataSourceSupport.DATABASE_PORT, databasePort).add(DataSourceSupport.DATABASE_NAME, databaseName).format();
            // jdbc:mysql://{database_ip}:{database_port}/{database_name}?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
            DataSourceProperty slaveProperty = new DataSourceProperty();
            slaveProperty.setUrl(realJdbcUrl);
            slaveProperty.setUsername(username);
            slaveProperty.setPassword(password);
            slaveProperty.setDriverClassName(properties.getDriverClassName());
            slaveProperty.setType(properties.getType());
            map.put(name, slaveProperty);
        }
    }

}
