package com.web.shop.config;

import com.web.shop.component.ShopHandlerInterceptor;
import com.web.shop.component.SignHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册用户后台管理拦截器
        registry.addInterceptor(new SignHandlerInterceptor()).addPathPatterns("/user/**").addPathPatterns("/shop/**");
        // 注册商家后台管理拦截器
        registry.addInterceptor(new ShopHandlerInterceptor()).addPathPatterns("/shop/**");
    }

}
