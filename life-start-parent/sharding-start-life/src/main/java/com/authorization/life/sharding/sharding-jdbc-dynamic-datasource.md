# Sharding-Jdbc水平分表+Mybatis-Plus+数据库中管理多数据源

## 序言

分库分表的原因就不在多说, 具体可以参考: [为什么要分库分表？](https://blog.csdn.net/weixin_44742132/article/details/124138347)

此次实现的是 mybatis-dynamic-datasource, 从数据库中读取从数据源信息. 搭建mysql的主从复制, 一主多从模式.

sharding-jdbc水平分表处理逻辑,以及遇到的问题.

## mysql8 windows 搭建主从复制模式

此处可参考:

[mysql8主从复制->一主多从](https://gitee.com/qjyn/hulun-buir/blob/master/hulunbuir-study-work/src/main/java/com/hulunbuir/study/doc/devops_install/mysql/mysql8_install/mysql8_install.md)

## springboot 2.7 集成 mybatis-plus 3.5.3 + dynamic-datasource 3.6.1

### dynamic-datasource 相关依赖

```xml
        <!--mysql + mybatis-plus start -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
        </dependency>
        <dependency>
            <groupId>com.zaxxer</groupId>
            <artifactId>HikariCP</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
        </dependency>
        <!--mysql + mybatis-plus end -->
        <!-- 动态数据源 - start   -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
        </dependency>
        <!-- 动态数据源 - end -->
```

## dynamic-datasource 相关配置

### 继承 AbstractJdbcDataSourceProvider 抽象类

1. loadDataSources() 为加载数据源的入口
2. protected Map<String, DataSourceProperty> executeStmt(Statement statement) 执行查询SQL(数据源查询语句)
3. setSalveDataSource() 设置从数据源信息
4. createDataSourceMapByDiy() 手动创建 HikariDataSource 数据源信息

#### 相关源码

```
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
            // 执行SQL语句,并获取数据源信息
            Map<String, DataSourceProperty> dataSourcePropertiesMap = executeStmt(stmt);
            // 处理为 HikariDataSource 数据源信息
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
```

### 配置信息入口, 自定义创建 DynamicDataSourceProvider

```
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider(StringEncryptor stringEncryptor,
                                                               DataSourceProperties dataSourceProperties,
                                                               DynamicDataSourceProperties dynamicDataSourceProperties) {
        return new JdbcDynamicDataSourceProvider(stringEncryptor, dataSourceProperties, dynamicDataSourceProperties);
    }
```

#### 此处也可以使用 @DS("数据源名称"), 查询是没有问题的. 在serviceImpl实现类中,添加即可生效.

数据源信息表:

```mysql
create table if not exists conf_datasource
(
    datasource_id   int auto_increment comment '主键'
        primary key,
    service_name    varchar(60)                        null comment '服务名称',
    datasource_name varchar(64)                        null comment 'SPRING中动态数据源的名称',
    database_name   varchar(64)                        null comment '数据库名称',
    database_ip     varchar(64)                        null comment '数据库IP',
    database_port   varchar(10)                        null comment '数据库端口号',
    username        varchar(64)                        null comment '用户名',
    password        varchar(64)                        null comment '密码',
    url             varchar(500)                       null comment 'jdbc_url',
    create_date     datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_date     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新',
    del_flag        char     default '0'               null comment '删除标记'
) comment '数据源表';
```

### springboot + dynamic-datasource 到此处结束

## springboot 2.7 集成 mybatis-plus 3.5.3 + dynamic-datasource 3.6.1 + sharding-jdbc 5.3.2

### 依赖

```xml
        <dependency>
            <groupId>org.yaml</groupId>
            <artifactId>snakeyaml</artifactId>
            <!-- 此处的版本号必须是 1.33, 官网提示也需要 1.33  -->
        </dependency>
         <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>shardingsphere-jdbc-core</artifactId>
            <version>5.3.2</version>
        </dependency>
```

### 配置信息

主要参考官网:

https://shardingsphere.apache.org/document/5.3.2/cn/user-manual/shardingsphere-jdbc/java-api/rules/readwrite-splitting

此处使用javaAPI进行配置










