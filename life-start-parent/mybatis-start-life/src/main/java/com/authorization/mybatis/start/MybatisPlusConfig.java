package com.authorization.mybatis.start;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;

/**
 * <p>
 * Explain:mybatis-plus的配置信息
 * </p >
 *
 * @author wangjunming
 * @since 2020-01-17 11:50
 */
@AutoConfiguration
@EnableTransactionManagement
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer(Environment environment) {
        return plusProperties -> {
            plusProperties.getGlobalConfig().setBanner(false);
            plusProperties.getConfiguration().setMapUnderscoreToCamelCase(true);
            if (Objects.isNull(environment.getProperty("mybatis-plus.global-config.db-config.id-type"))) {
                plusProperties.getGlobalConfig().getDbConfig().setIdType(IdType.AUTO);
            }
        };
    }

    @Bean
    @Order(100)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 防止全表更新
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // 添加乐观锁控制
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

}
