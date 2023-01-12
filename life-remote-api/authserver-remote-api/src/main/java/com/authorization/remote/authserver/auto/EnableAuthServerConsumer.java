package com.authorization.remote.authserver.auto;

import com.authorization.remote.authserver.config.AuthServerConsumerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * auth-life 服务消费接口的引用
 *
 * @author wangjunming
 * @date 2022/12/23 14:50
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AuthServerConsumerConfiguration.class)
public @interface EnableAuthServerConsumer {

}
