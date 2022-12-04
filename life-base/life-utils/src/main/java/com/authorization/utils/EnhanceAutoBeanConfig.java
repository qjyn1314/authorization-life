package com.authorization.utils;

import com.authorization.utils.json.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 增强bean配置类
 */
@Configuration
public class EnhanceAutoBeanConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return ObjectMappers.configMapper();
    }

}
