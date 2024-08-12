package com.authorization.utils.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 公共的环境变量
 *
 * @author wangjunming
 * @date 2023/4/10 15:42
 */
@Setter
@Getter
public class LifeProperties {

    @Value("${spring.application.name}")
    private String applicationName;

}
