package com.authorization.life.datasource.start.config;

import com.authorization.life.datasource.start.support.DataSourceSupport;
import com.authorization.utils.kvp.KvpFormat;
import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
import com.baomidou.dynamic.datasource.toolkit.ConfigMergeCreator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 从数据源中获取 配置信息
 */
@Slf4j
public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

    private final DataSourceProperties dataSourceProperties;
    private final StringEncryptor stringEncryptor;
    private final DynamicDataSourceProperties dynamicDataSourceProperties;

    public JdbcDynamicDataSourceProvider(StringEncryptor stringEncryptor, DataSourceProperties dataSourceProperties, DynamicDataSourceProperties dynamicDataSourceProperties) {
        super(dataSourceProperties.getDriverClassName(), dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
        this.stringEncryptor = stringEncryptor;
        this.dataSourceProperties = dataSourceProperties;
        this.dynamicDataSourceProperties = dynamicDataSourceProperties;
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
        property.setUrl(dataSourceProperties.getUrl());
        property.setUsername(dataSourceProperties.getUsername());
        property.setPassword(dataSourceProperties.getPassword());
        property.setDriverClassName(dataSourceProperties.getDriverClassName());
        property.setType(dataSourceProperties.getType());
        map.put(DataSourceSupport.DS_MASTER, property);

        ResultSet rs = null;
        try {
            // 从数据库中查询多数据源表获取数据源
            rs = statement.executeQuery(DataSourceSupport.QUERY_DS_SQL);
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
            log.info("数据库中的数据源->{}-密码->{}", name, password);
            // 使用自定义解密工具
            password = stringEncryptor.decrypt(password);
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
            slaveProperty.setDriverClassName(dataSourceProperties.getDriverClassName());
            slaveProperty.setType(dataSourceProperties.getType());
            map.put(name, slaveProperty);
        }
    }

    /**
     * 调用的总方法, 通过执行SQL语句获取多个数据源信息
     */
    @Override
    public Map<String, DataSource> loadDataSources() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 由于 SPI 的支持，现在已无需显示加载驱动了
            // 但在用户显示配置的情况下，进行主动加载
            if (StringUtils.hasText(dataSourceProperties.getDriverClassName())) {
                Class.forName(dataSourceProperties.getDriverClassName());
                log.info("成功加载数据库驱动程序");
            }
            conn = DriverManager.getConnection(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
            log.info("成功获取数据库连接");
            stmt = conn.createStatement();
            Map<String, DataSourceProperty> dataSourcePropertiesMap = executeStmt(stmt);
            return createDataSourceMapByDiy(dataSourcePropertiesMap);
        } catch (Exception e) {
            log.error("获取数据源失败,异常信息->", e);
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
        }
        return null;
    }

    private static final ConfigMergeCreator<HikariCpConfig, HikariConfig> MERGE_CREATOR = new ConfigMergeCreator<>("HikariCp", HikariCpConfig.class, HikariConfig.class);

    /**
     * 手动创建 HikariDataSource 数据源信息, 利用配置文件中的信息
     *
     * @param dataSourcePropertiesMap 项目中的所有数据源信息
     * @return Map<String, DataSource>
     */
    private Map<String, DataSource> createDataSourceMapByDiy(Map<String, DataSourceProperty> dataSourcePropertiesMap) {
        // 获取配置文件中的信息
        HikariCpConfig hikari = dynamicDataSourceProperties.getHikari();
        // 将配置文件中的信息设置到 原始 HikariConfig中
        HikariConfig hikariConfig = MERGE_CREATOR.create(hikari, hikari);
        Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();
        dataSourcePropertiesMap.forEach((sourceName, sourceVal) -> {
            hikariConfig.setJdbcUrl(sourceVal.getUrl());
            hikariConfig.setUsername(sourceVal.getUsername());
            hikariConfig.setPassword(sourceVal.getPassword());
            hikariConfig.setDriverClassName(sourceVal.getDriverClassName());
            hikariConfig.setPoolName(sourceName);
            HikariDataSource dataSource = new HikariDataSource(hikariConfig);
            dataSourceMap.put(sourceName, dataSource);
        });
        return dataSourceMap;
    }

}
