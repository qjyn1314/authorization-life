package com.authorization.life.sharding.config;

import cn.hutool.json.JSONUtil;
import com.authorization.life.datasource.start.support.DataSourceSupport;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.strategy.StaticReadwriteSplittingStrategyConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 读写分离, 使用权重算法进行配置查询的逻辑
 *
 * @author wangjunming
 * @date 2023/4/26 15:57
 */
@Slf4j
public class ReadWriteConfig {

    public DataSource getDataSource(DynamicDataSourceProvider dynamicDataSourceProvider, Properties props) throws SQLException {
        log.info("开始创建数据源->com.hulunbuir.sharding.ShardingAutoConfig.dataSource->start");
        Map<String, DataSource> stringDataSourceMap = dynamicDataSourceProvider.loadDataSources();

        // 从数据库的数据源名称集合
        List<String> slaveList = stringDataSourceMap.keySet().stream().filter(item -> !DataSourceSupport.DS_MASTER.equals(item)).collect(Collectors.toList());
        log.info("加载的 从数据源 信息是->{}", JSONUtil.toJsonStr(slaveList));
        StaticReadwriteSplittingStrategyConfiguration staticStrategy = new StaticReadwriteSplittingStrategyConfiguration(DataSourceSupport.DS_MASTER, slaveList);

        String dataSourceConfigName = "test_read_query_ds";

        String loadBalancerName = "demo_weight_lb";

        ReadwriteSplittingDataSourceRuleConfiguration dataSourceConfig = new ReadwriteSplittingDataSourceRuleConfiguration(
                dataSourceConfigName, staticStrategy, null, loadBalancerName);

        Properties algorithmProps = new Properties();
        for (int i = 0; i < slaveList.size(); i++) {
            algorithmProps.setProperty(slaveList.get(i), String.valueOf(i + 1));
        }

        Map<String, AlgorithmConfiguration> algorithmConfigMap = new HashMap<>(1);

        algorithmConfigMap.put(loadBalancerName, new AlgorithmConfiguration("WEIGHT", algorithmProps));
        ReadwriteSplittingRuleConfiguration ruleConfig = new ReadwriteSplittingRuleConfiguration(Collections.singleton(dataSourceConfig), algorithmConfigMap);

        DataSource dataSource = ShardingSphereDataSourceFactory.createDataSource(stringDataSourceMap, Collections.singleton(ruleConfig), props);

        log.info("创建-ShardingSphereDataSource-成功->{}", JSONUtil.toJsonStr(dataSource));
        return dataSource;
    }

}