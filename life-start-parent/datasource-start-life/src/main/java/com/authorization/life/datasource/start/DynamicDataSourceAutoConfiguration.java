package com.authorization.life.datasource.start;

import com.authorization.life.datasource.start.config.JdbcDynamicDataSourceProvider;
import com.authorization.life.datasource.start.config.LastParamDsProcessor;
import com.authorization.utils.jasypt.JasyptConfig;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 自定义动态数据源切换配置, 从数据库中获取数据源信息
 * 重点:
 * https://juejin.cn/post/7135699186070552590
 * 配置参考:
 * https://www.jianshu.com/p/ab6dbe9e7bd5
 * <p>
 * https://github.com/baomidou/dynamic-datasource-spring-boot-starter/blob/master/test/javax/src/test/java/com/baomidou/dynamic/datasource/test/javax/LoadDatasourceFromJDBCTest.java
 * <p>
 * 手动切换数据源
 * DynamicDataSourceContextHolder.push("slave");
 *
 * @author wangjunming
 */
@Slf4j
@Import(JasyptConfig.class)
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({DataSourceProperties.class, DynamicDataSourceProperties.class})
public class DynamicDataSourceAutoConfiguration {

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider(StringEncryptor stringEncryptor,
                                                               DataSourceProperties dataSourceProperties,
                                                               DynamicDataSourceProperties dynamicDataSourceProperties) {
        return new JdbcDynamicDataSourceProvider(stringEncryptor, dataSourceProperties, dynamicDataSourceProperties);
    }

    @Bean
    public DsProcessor dsProcessor() {
        return new LastParamDsProcessor();
    }

}
