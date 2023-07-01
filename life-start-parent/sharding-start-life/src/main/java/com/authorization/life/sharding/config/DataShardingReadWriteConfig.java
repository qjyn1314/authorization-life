package com.authorization.life.sharding.config;

import cn.hutool.json.JSONUtil;
import com.authorization.life.datasource.start.support.DataSourceSupport;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.strategy.StaticReadwriteSplittingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 读写分离 + 分表
 *
 * @author wangjunming
 * @date 2023/6/13 15:22
 */
@Slf4j
public class DataShardingReadWriteConfig {


    public DataSource getDataSource(DynamicDataSourceProvider dynamicDataSourceProvider, Properties props) throws SQLException {
        log.info("开始创建数据源->com.hulunbuir.sharding.ShardingAutoConfig.dataSource->start");
        Map<String, DataSource> dataSourceMap = dynamicDataSourceProvider.loadDataSources();
        // 整个 sharding配置的逻辑数据源名称,
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
        props.put("algorithmClassName", "com.authorization.life.sharding.config.TenantIdPreciseShardingAlgorithm");
        result.getShardingAlgorithms().put(tableShardingAlgorithmsName, new AlgorithmConfiguration("CLASS_BASED", props));

        // https://shardingsphere.apache.org/document/current/cn/user-manual/common-config/builtin-algorithm/keygen/
        // 自增列生成算法名称和配置
//        result.getKeyGenerators().put("snowflake", new AlgorithmConfiguration("SNOWFLAKE", new Properties()));
        log.info("配置的分表信息是->{}", JSONUtil.toJsonStr(result));
        return result;
    }

    private ShardingTableRuleConfiguration getLemdEmpTableRuleConfiguration(String dataSourceConfigName,String tableShardingAlgorithmsName) {
        // 分片逻辑表名称
        String logicTable = "lemd_emp";
        // 由数据源名 + 表名组成，以小数点分隔。多个表以逗号分隔，支持行表达式
        String actualDataNodes = dataSourceConfigName + ".lemd_emp_$->{0..9999}";
        ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration(logicTable, actualDataNodes);
        // 默认分表策略-- 查询数据是必须带有此分表列作为查询条件
        result.setTableShardingStrategy(new StandardShardingStrategyConfiguration("tenant_id", tableShardingAlgorithmsName));
//        // 默认自增列生成器配置
//        result.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("emp_id", "snowflake"));
        return result;
    }

}