package com.authorization.life.sharding.annotion;

import com.authorization.life.sharding.ShardingAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 引入sharding-jdbc功能注解
 *
 * @author wangjunming
 * @date 2023/4/3 15:32
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ShardingAutoConfig.class)
public @interface EnableShardingJdbc {
}