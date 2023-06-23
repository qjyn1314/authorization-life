package com.authorization.life.system.infra.util;

import com.authorization.common.config.RestTemplateConfig;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate工具类, 用于创建 RestTemplate
 *
 * @author wangjunming
 * @date 2023/4/18 16:57
 */
public class RestTempUtil {

    private RestTempUtil() {
    }

    /**
     * rest模板
     *
     * @return RestTemplate
     */
    public static RestTemplate restTemplate() {
        return RestTemplateConfig.getStaticRest();
    }


}