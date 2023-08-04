# Sharding-Jdbc水平分表+Mybatis-Plus+数据库管理多数据源

## 序言

分库分表的原因就不再多说,
具体可以参考: [为什么要分库分表？](https://blog.csdn.net/weixin_44742132/article/details/124138347)

此次实现的是 mybatis-dynamic-datasource, 从数据库中读取多个从数据源信息, 形成多数据源. 

为读写分离做准备, 搭建mysql的主从复制, 一主多从模式.

集成sharding-jdbc水平分表处理逻辑,以及遇到的问题.

租户注册时进行创建相关分表的思路.

## mysql8 windows 搭建主从复制模式

此处可参考:

[mysql8主从复制->一主多从](https://gitee.com/qjyn/hulun-buir/blob/master/hulunbuir-study-work/src/main/java/com/hulunbuir/study/doc/devops_install/mysql/mysql8_install/mysql8_install.md)

## springboot 2.7 集成 mybatis-plus 3.5.3 + dynamic-datasource 3.6.1

### dynamic-datasource 和 mybatis-plus 相关依赖

```java
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

## 从数据库中读取数据源信息  springboot + dynamic-datasource 集成到此处结束........

### 以上集成注意事项: 

###### 对于注解 @DS("数据源名称") 是可以使用的, 但是 集成了 sharding-jdbc 后就不再有效果了. 其原因主要是因为 集成之前动态数据源信息的数据源是 DynamicRoutingDataSource, 在集成之后就变成了 DataShardingReadWriteConfig 创建出来的数据源,其创建的过程不一致, 返回的数据源内容也不尽相同.

## springboot 2.7 集成 mybatis-plus 3.5.3 + dynamic-datasource 3.6.1 + sharding-jdbc 5.3.2 (最新版)

### 依赖

```
        <dependency>
            <groupId>org.yaml</groupId>
            <artifactId>snakeyaml</artifactId>
            <!-- 此处的版本号必须是 1.33, 官网提示也需要 1.33, 即需要在父工程的pom文件中重新定义此依赖的版本号 -->
            <version>1.33</version>
        </dependency>
         <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>shardingsphere-jdbc-core</artifactId>
            <version>5.3.2</version>
        </dependency>
```

### 配置信息

参考官网: [sharding-jdbc的读写分离JavaApi配置信息](https://shardingsphere.apache.org/document/5.3.2/cn/user-manual/shardingsphere-jdbc/java-api/rules/readwrite-splitting)

##### 以下介绍如何使用javaAPI进行配置sharding-jdbc的分库分表和读写分离, 以及在集成过程中遇到的问题. 



#### sharding-jdbc的数据源配置信息
```

    public DataSource getDataSource(DynamicDataSourceProvider dynamicDataSourceProvider, Properties props) throws SQLException {
        Map<String, DataSource> dataSourceMap = dynamicDataSourceProvider.loadDataSources();
        // 整个 sharding配置的逻辑数据源名称, ******此处为重点之一
        String dataSourceConfigName = "diy-sharding-read-write-datasource";
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, getRuleConfigs(dataSourceConfigName, dataSourceMap), props);
    }

    private Collection<RuleConfiguration> getRuleConfigs(String dataSourceConfigName, Map<String, DataSource> dataSourceMap) {
        List<RuleConfiguration> ruleConfigs = new ArrayList<>();
        //分库分表逻辑
        ruleConfigs.add(createShardingRuleConfiguration(dataSourceConfigName, dataSourceMap));
        //读写分离配置逻辑
        ruleConfigs.add(createReadwriteSplittingRuleConfiguration(dataSourceConfigName, dataSourceMap));

        return ruleConfigs;
    }

    private RuleConfiguration createReadwriteSplittingRuleConfiguration(String dataSourceConfigName, Map<String, DataSource> dataSourceMap) {
        // 从数据库的数据源名称集合
        List<String> slaveList = dataSourceMap.keySet().stream().filter(item -> !DataSourceSupport.DS_MASTER.equals(item)).collect(Collectors.toList());
        log.info("加载的 从数据源 信息是->{}", JSONUtil.toJsonStr(slaveList));
        // 静态读写分离配置(写库数据源名称,读库数据源列表)
        StaticReadwriteSplittingStrategyConfiguration staticStrategy = new StaticReadwriteSplittingStrategyConfiguration(DataSourceSupport.DS_MASTER, slaveList);

        // 算法名称
        String loadBalancerName = "qs-load-balancers-name";

        ReadwriteSplittingDataSourceRuleConfiguration dataSourceConfig = new ReadwriteSplittingDataSourceRuleConfiguration(
                dataSourceConfigName, staticStrategy, null, loadBalancerName);

// 参考官网:  https://shardingsphere.apache.org/document/current/cn/user-manual/common-config/builtin-algorithm/load-balance/
//        // 读写分离的算法-权重算法
//        Properties algorithmProps = new Properties();
//        for (int i = 0; i < slaveList.size(); i++) {
//            algorithmProps.setProperty(slaveList.get(i), String.valueOf(i + 5));
//        }
//        // 说明：事务内，读请求根据 transaction-read-query-strategy 属性的配置进行路由。事务外，采用权重策略路由到 replica。
//        algorithmProps.put("transaction-read-query-strategy", "FIXED_PRIMARY");
//        algorithmProps.put("slave02", algorithmProps.getProperty("slave02"));
//        Map<String, AlgorithmConfiguration> algorithmConfigMap = new HashMap<>(1);
//        algorithmConfigMap.put(loadBalancerName, new AlgorithmConfiguration("WEIGHT", algorithmProps));

        // 读写分离算法 - 随机负载均衡算法
//        Map<String, AlgorithmConfiguration> algorithmConfigMap = new HashMap<>(1);
////        Properties algorithmProps = new Properties();
//         // 说明：事务内，读请求根据 transaction-read-query-strategy 属性的配置进行路由。事务外，采用权重策略路由到 replica。
//        algorithmProps.put("transaction-read-query-strategy", "FIXED_PRIMARY");
//        algorithmProps.put("slave02", algorithmProps.getProperty("slave02"));
//        algorithmConfigMap.put(loadBalancerName, new AlgorithmConfiguration("RANDOM", algorithmProps));

        //负载均衡算法配置
        Map<String, AlgorithmConfiguration> loadBalanceMap = new HashMap<>();
        Properties properties = new Properties();
        for (int i = 0; i < slaveList.size(); i++) {
            properties.setProperty(slaveList.get(i), String.valueOf(i + 5));
        }
        // ******此处为重点之一
        properties.put("transaction-read-query-strategy", "FIXED_PRIMARY");
        loadBalanceMap.put(loadBalancerName, new AlgorithmConfiguration("ROUND_ROBIN", properties));

        ReadwriteSplittingRuleConfiguration ruleConfig = new ReadwriteSplittingRuleConfiguration(Collections.singleton(dataSourceConfig), loadBalanceMap);
        log.info("配置的读写分离信息是->{}", JSONUtil.toJsonStr(ruleConfig));
        return ruleConfig;
    }


    /**
     * 数据库分表配置
     */
    private ShardingRuleConfiguration createShardingRuleConfiguration(String dataSourceConfigName, Map<String, DataSource> dataSourceMap) {
        ShardingRuleConfiguration result = new ShardingRuleConfiguration();
        String tableShardingAlgorithmsName = "standard_test_tbl";
        // 分片表规则列表
        result.getTables().add(getLemdEmpTableRuleConfiguration(dataSourceConfigName, tableShardingAlgorithmsName));

        // 原生分表逻辑算法配置
//        Properties props = new Properties();
//        props.put("algorithm-expression","lemd_emp_${tenant_id}");
//        result.getShardingAlgorithms().put(tableShardingAlgorithmsName, new AlgorithmConfiguration("INLINE", props));

        // 自定义分表算法配置
        Properties props = new Properties();
        props.put("strategy", "Standard");
        // ******此处为重点之一
        props.put("algorithmClassName", "com.authorization.life.sharding.config.TenantIdPreciseShardingAlgorithm");
        result.getShardingAlgorithms().put(tableShardingAlgorithmsName, new AlgorithmConfiguration("CLASS_BASED", props));

        // https://shardingsphere.apache.org/document/5.3.2/cn/user-manual/common-config/builtin-algorithm/keygen/
        // 自增列生成算法名称和配置
//        result.getKeyGenerators().put("snowflake", new AlgorithmConfiguration("SNOWFLAKE", new Properties()));
        log.info("配置的分表信息是->{}", JSONUtil.toJsonStr(result));
        return result;
    }

    private ShardingTableRuleConfiguration getLemdEmpTableRuleConfiguration(String dataSourceConfigName,String tableShardingAlgorithmsName) {
        // 分片逻辑表名称
        String logicTable = "lemd_emp";
        // 由数据源名 + 表名组成，以小数点分隔。多个表以逗号分隔，支持行表达式   ******此处为重点之一 
        String actualDataNodes = dataSourceConfigName + ".lemd_emp_$->{0..9999}";
        ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration(logicTable, actualDataNodes);
        // 默认分表策略-- 查询数据是必须带有此分表列作为查询条件
        result.setTableShardingStrategy(new StandardShardingStrategyConfiguration("tenant_id", tableShardingAlgorithmsName));
//        // 默认自增列生成器配置
//        result.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("emp_id", "snowflake"));
        return result;
    }

```


#### sharding-jdbc的数据源配置信息的总入口


```

@Slf4j
@Import(DynamicDataSourceAutoConfiguration.class)
@AutoConfiguration
public class ShardingAutoConfig {

    private Properties props() {
        Properties props = new Properties();
        //是否在日志中打印 SQL。
        //打印 SQL 可以帮助开发者快速定位系统问题。日志内容包含：逻辑 SQL，真实 SQL 和 SQL 解析结果。
        //如果开启配置，日志将使用 Topic ShardingSphere-SQL，日志级别是 INFO。
        props.setProperty("sql-show", Boolean.TRUE.toString());
        // 是否在日志中打印简单风格的 SQL。
        props.setProperty("sql-simple", Boolean.FALSE.toString());
        return props;
    }

    /**
     * 读写分离配置参考:
     * <a href="https://shardingsphere.apache.org/document/5.3.2/cn/user-manual/shardingsphere-jdbc/java-api/rules/readwrite-splitting/">读写分离官网</a>
     *
     * @param dynamicDataSourceProvider 用于加载数据库中的数据源
     * @return DataSource
     * @throws SQLException
     */
    @Primary
    @Bean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) throws SQLException {
        return new DataShardingReadWriteConfig().getDataSource(dynamicDataSourceProvider, props());
    }

}

```

### 整合sharding-jdbc过程中遇到的问题

1. 字段 actualDataNodes 的表达式书写 重点之一:  $->
示例 : lemd_emp_$->{0..9999}

官网: 
https://shardingsphere.apache.org/document/5.3.2/cn/user-manual/shardingsphere-jdbc/java-api/rules/sharding/

在官网中没有具体的体现出来, 官网上并没有加  ->  符号.

2. 设置 sql-show 为 true ,  sql-simple  为 false  .

在配置成功之后会打印执行的预编译sql语句, sql语句中的表名是真实的表名.

3. 自定义分表算法配置中, 算法类型为  CLASS_BASED  时, 才进行编写自定义算法类.

可参考:

com.authorization.life.sharding.config.TenantIdPreciseShardingAlgorithm

4. 在对于被分表字段, 在增删改查的时候, 其字段(例如: tenant_id )必须不能为空(包含空字符串).


### 租户注册是的流程以及需要注意事项

1. 对于 sharding-jdbc 是没有自动生成表操作的.

在租户注册的时候需要执行创建表sql语句, 进行创建相关分表. 即 将创建表sql写在配置文件中, 或者写在枚举类中, 将租户ID进行配置得到最终的创建表sql, 进行创建相关分表.

