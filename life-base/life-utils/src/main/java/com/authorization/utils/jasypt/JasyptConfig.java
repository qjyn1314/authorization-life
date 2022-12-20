package com.authorization.utils.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 密码的密文配置信息
 *
 * @author wangjunming
 * @date 2022/12/20 15:39
 */
@Configuration
public class JasyptConfig {

    /**
     * 自定义 StringEncryptor，覆盖默认的 StringEncryptor
     * bean 名称是必需的，从 1.5 版开始按名称检测自定义字符串加密程序,默认 bean 名称为：jasyptStringEncryptor
     *
     * 使用: Jasypt加密，格式为ENC(加密结果)
     *
     *  ENC(密文)
     *
     * @return StringEncryptor
     */
    @Bean("jasyptStringEncryptor")
    public StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(JasyptUtils.getSimpleStringPBEConfig(JasyptUtils.SECRET_KEY));
        return encryptor;
    }

}