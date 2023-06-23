package com.authorization.utils.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * TODO 请填写类描述
 *
 * @author wangjunming
 * @date 2023/4/10 15:42
 */
@Setter
@Getter
@Configuration
public class LifeProperties {

    @Value("${spring.application.name}")
    private String applicationName;

}