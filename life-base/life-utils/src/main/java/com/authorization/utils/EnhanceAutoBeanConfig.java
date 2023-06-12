package com.authorization.utils;

import com.authorization.utils.json.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 增强bean配置类
 */
@AutoConfiguration
public class EnhanceAutoBeanConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return ObjectMappers.configMapper();
    }

}
