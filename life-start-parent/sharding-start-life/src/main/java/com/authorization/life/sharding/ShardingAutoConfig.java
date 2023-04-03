package com.authorization.life.sharding;

import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.infra.config.mode.PersistRepositoryConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.mode.repository.standalone.StandalonePersistRepositoryConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * sharding-jdbc的自动配置
 *
 * @author wangjunming
 * @date 2023/4/3 15:32
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@AutoConfiguration
public class ShardingAutoConfig {

    @Bean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider,
                                 ModeConfiguration modeConfig) throws SQLException {
        String databaseName = "lifeDataSource";
        Map<String, DataSource> dataSourceMap = dynamicDataSourceProvider.loadDataSources();
        Collection<RuleConfiguration> ruleConfigs = createRuleConfiguration();
        Properties props = createProperties();
        DataSource dataSource = ShardingSphereDataSourceFactory.createDataSource(databaseName, modeConfig, dataSourceMap, ruleConfigs, props);
        log.info("dataSource-->{}", JSONUtil.toJsonStr(dataSource));
        return dataSource;
    }

    private Collection<RuleConfiguration> createRuleConfiguration() {
        return new ArrayList<>();
    }

    private Properties createProperties() {
        return new Properties();
    }

    @Bean
    public ModeConfiguration createModeConfiguration(PersistRepositoryConfiguration repositoryConfiguration) {
        return new ModeConfiguration("Standalone", repositoryConfiguration);
    }

    @Bean
    public PersistRepositoryConfiguration repositoryConfiguration() {
        return new StandalonePersistRepositoryConfiguration("JDBC", new Properties());
    }


}