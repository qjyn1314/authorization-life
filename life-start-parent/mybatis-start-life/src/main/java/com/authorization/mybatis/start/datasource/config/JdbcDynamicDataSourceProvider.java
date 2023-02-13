package com.authorization.mybatis.start.datasource.config;

import com.authorization.mybatis.start.datasource.support.DataSourceConstants;
import com.authorization.utils.json.JsonHelper;
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
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(properties.getQueryDsSql());
            log.info("执行后的结果是-{}", JsonHelper.toJson(rs));
        } catch (SQLException e) {
            log.error("查询从数据源失败...", e);
        }
        Map<String, DataSourceProperty> map = new ConcurrentHashMap<>(8);

        if (Objects.nonNull(rs)) {
            while (rs.next()) {
                String name = rs.getString(DataSourceConstants.DS_NAME);
                String username = rs.getString(DataSourceConstants.DS_USER_NAME);
                String password = rs.getString(DataSourceConstants.DS_USER_PWD);
                String url = rs.getString(DataSourceConstants.DS_JDBC_URL);
                DataSourceProperty property = new DataSourceProperty();
                property.setUsername(username);
                property.setLazy(true);
                property.setPassword(stringEncryptor.decrypt(password));
                property.setUrl(url);
                map.put(name, property);
            }
        }


        // 添加默认主数据源
        DataSourceProperty property = new DataSourceProperty();
        property.setUsername(properties.getUsername());
        property.setPassword(properties.getPassword());
        property.setUrl(properties.getUrl());
        property.setLazy(true);
        map.put(DataSourceConstants.DS_MASTER, property);
        return map;
    }

}
