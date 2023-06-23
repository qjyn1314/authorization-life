package com.authorization.life.sharding.config;

import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * 数据分片配置
 *
 * @author wangjunming
 * @date 2023/4/26 16:01
 */
public class DataShardingConfig {

    public DataSource getDataSource(DynamicDataSourceProvider dynamicDataSourceProvider, Properties props) throws SQLException {
        Map<String, DataSource> stringDataSourceMap = dynamicDataSourceProvider.loadDataSources();
        return ShardingSphereDataSourceFactory.createDataSource(stringDataSourceMap, Collections.singleton(createShardingRuleConfiguration()), props);
    }

    private ShardingRuleConfiguration createShardingRuleConfiguration() {
        ShardingRuleConfiguration result = new ShardingRuleConfiguration();
        String tableShardingAlgorithmsName = "standard_test_tbl";
        // 分片表规则列表
        result.getTables().add(getLemdEmpTableRuleConfiguration(tableShardingAlgorithmsName));

        // 原生分表逻辑算法配置
//        Properties props = new Properties();
//        props.put("algorithm-expression","lemd_emp_${tenant_id}");
//        result.getShardingAlgorithms().put(tableShardingAlgorithmsName, new AlgorithmConfiguration("INLINE", props));

        // 自定义分表算法配置
        Properties props = new Properties();
        props.put("strategy", "Standard");
        props.put("algorithmClassName", "com.hulunbuir.sharding.config.TenantIdPreciseShardingAlgorithm");
        result.getShardingAlgorithms().put(tableShardingAlgorithmsName, new AlgorithmConfiguration("CLASS_BASED", props));

        // https://shardingsphere.apache.org/document/current/cn/user-manual/common-config/builtin-algorithm/keygen/
        // 自增列生成算法名称和配置
//        result.getKeyGenerators().put("snowflake", new AlgorithmConfiguration("SNOWFLAKE", new Properties()));

        return result;
    }

    private ShardingTableRuleConfiguration getLemdEmpTableRuleConfiguration(String tableShardingAlgorithmsName) {
        // 分片逻辑表名称
        String logicTable = "lemd_emp";
        // 由数据源名 + 表名组成，以小数点分隔。多个表以逗号分隔，支持行表达式
        String actualDataNodes = "master.lemd_emp_$->{0..999}";
        ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration(logicTable, actualDataNodes);
        // 默认分表策略-- 查询数据是必须带有此分表列作为查询条件
        result.setTableShardingStrategy(new StandardShardingStrategyConfiguration("tenant_id", tableShardingAlgorithmsName));
        return result;
    }

}