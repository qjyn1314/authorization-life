package com.authorization.valid.start.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;


/**
 * 请求参数的验证配置类
 *
 * @author wangjunming
 * @date 2022/12/22 17:39
 */
@AutoConfiguration
public class ValidConfig {

    /**
     * 参考: https://turbosnail.top/blog/hibernate-validator-usage
     *
     * @return Validator
     */
    @Bean
    public Validator validator() {
        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class).configure()
                // 快速失败模式 将fail_fast设置为true即可，如果想验证全部，则设置为false(默认)或者取消配置即可
                // 快速失败模式在校验过程中，当遇到第一个不满足条件的参数时就立即返回，不再继续后面参数的校验。否则会一次性校验所有参数，并返回所有不符合要求的错误信息
                .failFast(true).buildValidatorFactory();
        return factory.getValidator();
    }

}
