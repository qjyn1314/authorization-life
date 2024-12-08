package com.authorization.life.lov.start.lov;

import com.authorization.life.lov.start.lov.aspect.TranslateLovAspect;
import com.authorization.life.lov.start.lov.handler.DefaultLovTranslator;
import com.authorization.life.lov.start.lov.handler.LovTranslator;
import com.authorization.life.lov.start.lov.helper.LovUserHelper;
import com.authorization.life.lov.start.lov.helper.LovUserHelperImpl;
import com.authorization.life.lov.start.lov.service.LovService;
import com.authorization.life.lov.start.lov.service.LovServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2024-12-08 16:38
 */
@Configuration
public class LovAutoConfig {

    @Bean
    public LovUserHelper lovUserHelper() {
        return new LovUserHelperImpl();
    }

    @Bean
    public LovService lovService() {
        return new LovServiceImpl();
    }

    @Bean
    public LovTranslator lovTranslator() {
        return new DefaultLovTranslator();
    }

    @Bean
    public TranslateLovAspect lovAspect() {
        return new TranslateLovAspect();
    }

}
