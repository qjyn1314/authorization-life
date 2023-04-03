package com.authorization.life.datasource.start.annotion;

import com.authorization.life.datasource.start.DynamicDataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 动态数据源的引入类
 *
 * @author wangjunming
 * @date 2023/3/29 17:22
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DynamicDataSourceAutoConfiguration.class)
public @interface EnableDynamicDataSource {
}