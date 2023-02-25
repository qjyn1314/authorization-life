package com.authorization.mybatis.start.datasource.config;

import com.authorization.mybatis.start.datasource.support.DataSourceConstants;
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
        log.info("JDBC中加载动态数据源.");
        Map<String, DataSourceProperty> map = new ConcurrentHashMap<>(8);
        // 添加默认主数据源
        DataSourceProperty property = new DataSourceProperty();
        property.setUrl(properties.getUrl());
        property.setUsername(properties.getUsername());
        property.setPassword(properties.getPassword());
        property.setDriverClassName(properties.getDriverClassName());
        property.setType(properties.getType());
        map.put(DataSourceConstants.DS_MASTER, property);

        ResultSet rs = null;
        try {
            // 从数据库中查询多数据源表获取数据源
            rs = statement.executeQuery(properties.getQueryDsSql());
        } catch (SQLException e) {
            log.error("查询从数据源失败...{}", e.getMessage());
        }
        if (Objects.isNull(rs)) {
            return map;
        }
        while (rs.next()) {
            String name = rs.getString(DataSourceConstants.DS_NAME);
            String url = rs.getString(DataSourceConstants.DS_JDBC_URL);
            String username = rs.getString(DataSourceConstants.DS_USER_NAME);
            String password = rs.getString(DataSourceConstants.DS_USER_PWD);
            DataSourceProperty slaveProperty = new DataSourceProperty();
            slaveProperty.setUrl(url);
            slaveProperty.setUsername(username);
            slaveProperty.setPassword(password);
            slaveProperty.setDriverClassName(properties.getDriverClassName());
            slaveProperty.setType(properties.getType());
            map.put(name, slaveProperty);
        }
        return map;
    }

}
