package com.authorization.core.intercept;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 可添加多个拦截器
        registry.addInterceptor(new UrlInfoInterceptor()).addPathPatterns("/**");
    }

}