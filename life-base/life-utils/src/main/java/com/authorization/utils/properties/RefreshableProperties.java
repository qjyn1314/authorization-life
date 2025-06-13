package com.authorization.utils.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * 可实时刷新公共的环境变量
 *
 * @author wangjunming
 * @date 2023/4/10 15:42
 */
@Setter
@Getter
public class RefreshableProperties {

    /**
     * 公共变量中国际化配置
     */
    @Value("${spring.application.name}")
    private String applicationName;

}
