package com.authorization.life.sharding;

import cn.hutool.json.JSONUtil;
import com.authorization.life.datasource.start.support.DataSourceSupport;
import com.authorization.utils.props.LifeProperties;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.database.type.DatabaseTypeEngine;
import org.apache.shardingsphere.readwritesplitting.algorithm.loadbalance.RandomReadQueryLoadBalanceAlgorithm;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.strategy.StaticReadwriteSplittingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * sharding-jdbc的自动配置
 * 参考:
 * <a href="https://shardingsphere.apache.org/document/5.3.0/cn/overview/">sharding官网最新文档</a>
 *
 * @author wangjunming
 * @date 2023/4/3 15:32
 */
@Slf4j
@AutoConfiguration
public class ShardingAutoConfig {

    @Resource
    private LifeProperties lifeProperties;

    private Properties createProperties() {
        Properties props = new Properties();
        // #是否开启SQL显示，默认值: false
        props.setProperty("sql-show", Boolean.TRUE.toString());
        // #是否在启动时检查分表元数据一致性，默认值: false
        props.setProperty("check-table-metadata-enabled", Boolean.TRUE.toString());
        return props;
    }

    @Bean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) throws SQLException {
        // 数据源
        Map<String, DataSource> dataSourceMap = dynamicDataSourceProvider.loadDataSources();
        // 规则
        Collection<RuleConfiguration> ruleConfigs = createRuleConfiguration(dataSourceMap);
        // 其他字段配置
        Properties props = createProperties();
        // Sharding 创建的数据源
        DataSource dataSource = ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, ruleConfigs, props);
        log.info("dataSource-->{}", JSONUtil.toJsonStr(dataSource));
        return dataSource;
    }

    /**
     * 获取规则配置
     */
    private Collection<RuleConfiguration> createRuleConfiguration(Map<String, DataSource> dataSourceMap) {
        ArrayList<RuleConfiguration> configurations = new ArrayList<>();

        // 获取读写分离配置
        ReadwriteSplittingRuleConfiguration readwriteSplittingRule = getReadwriteSplittingRule(dataSourceMap);
        if (Objects.nonNull(readwriteSplittingRule)) {
            configurations.add(readwriteSplittingRule);
        }

        // 获取分片规则
        ShardingRuleConfiguration shardingRuleConfig = getShardingRuleConfiguration(dataSourceMap);
        if (Objects.nonNull(shardingRuleConfig)) {
            configurations.add(shardingRuleConfig);
        }

        return configurations;
    }

    /**
     * <a href="https://shardingsphere.apache.org/document/5.3.0/cn/user-manual/shardingsphere-jdbc/java-api/rules/readwrite-splitting/">参考官网</a>
     * 获取读写分离配置
     */
    private ReadwriteSplittingRuleConfiguration getReadwriteSplittingRule(Map<String, DataSource> dataSourceMap) {
        // todo 此处将通过 读写分离字典配置 + 应用名称 获取读写分离配置
        String readwriteSplit = "READWRITE_SPLIT";
        String applicationName = lifeProperties.getApplicationName();

        // 主数据源--以 master 开头的认为是 主数据源
        Set<String> master = dataSourceMap.keySet().stream().filter(DataSourceSupport.DS_MASTER::startsWith).collect(Collectors.toSet());
        // 从数据库
        List<String> slave = dataSourceMap.keySet().stream().filter(item -> !item.startsWith(DataSourceSupport.DS_MASTER)).collect(Collectors.toList());

        // 静态读写分离配置
        StaticReadwriteSplittingStrategyConfiguration staticStrategy = new StaticReadwriteSplittingStrategyConfiguration(DataSourceSupport.DS_MASTER, slave);

        // 读写分离数据源名称
        String readwriteSplitName = "life_read_query_ds";
        ReadwriteSplittingDataSourceRuleConfiguration dataSourceConfig = new ReadwriteSplittingDataSourceRuleConfiguration(
                readwriteSplitName, staticStrategy, null, "RANDOM_READ_QUERY_LOAD_BALANCE");


        Properties algorithmProps = new Properties();
        for (int i = 0; i < slave.size(); i++) {
            algorithmProps.setProperty(slave.get(i), String.valueOf(i));
        }
        Map<String, AlgorithmConfiguration> algorithmConfigMap = new HashMap<>(1);
        // 使用 随机读查询负载均衡算法
        algorithmConfigMap.put("RANDOM_READ_QUERY_LOAD_BALANCE", new AlgorithmConfiguration(new RandomReadQueryLoadBalanceAlgorithm().getType(), algorithmProps));

        return new ReadwriteSplittingRuleConfiguration(Collections.singleton(dataSourceConfig), algorithmConfigMap);
    }

    /**
     * 获取分片规则
     */
    private ShardingRuleConfiguration getShardingRuleConfiguration(Map<String, DataSource> dataSourceMap) {
        return null;
    }

    //-------------------------------------------------------
//    @Bean
//    public ModeConfiguration createModeConfiguration(PersistRepositoryConfiguration repositoryConfiguration) {
//        return new ModeConfiguration("Standalone", repositoryConfiguration);
//    }
//
//    @Bean
//    public PersistRepositoryConfiguration repositoryConfiguration() {
//        return new StandalonePersistRepositoryConfiguration("JDBC", new Properties());
//    }


}