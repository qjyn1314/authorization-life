package com.authorization.remote.system.auto;

import com.authorization.remote.system.config.SystemProviderConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * system-life 服务提供接口的引用
 *
 * @author wangjunming
 * @date 2022/12/23 14:50
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SystemProviderConfiguration.class)
public @interface EnableSystemProvider {

}