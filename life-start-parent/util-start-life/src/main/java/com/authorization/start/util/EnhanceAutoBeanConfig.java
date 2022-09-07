package com.authorization.start.util;

import com.authorization.start.util.json.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 增强bean配置类
 */
@Configuration
public class EnhanceAutoBeanConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return ObjectMappers.configMapper();
    }

}
