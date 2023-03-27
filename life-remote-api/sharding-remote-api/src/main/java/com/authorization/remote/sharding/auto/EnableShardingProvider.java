package com.authorization.remote.sharding.auto;

import com.authorization.remote.sharding.config.ShardingProviderConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * sharding-life 服务提供接口的引用
 *
 * @author wangjunming
 * @date 2022/12/23 14:50
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ShardingProviderConfiguration.class)
public @interface EnableShardingProvider {

}
