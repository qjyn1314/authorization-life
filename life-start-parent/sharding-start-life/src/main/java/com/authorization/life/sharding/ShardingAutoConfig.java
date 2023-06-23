package com.authorization.life.sharding;


import com.authorization.life.datasource.start.DynamicDataSourceAutoConfiguration;
import com.authorization.life.sharding.config.DataShardingReadWriteConfig;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

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