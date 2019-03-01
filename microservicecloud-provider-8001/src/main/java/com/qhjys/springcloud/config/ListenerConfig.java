package com.qhjys.springcloud.config;

import com.qhjys.springcloud.listener.CharInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ListenerConfig extends WebMvcConfigurerAdapter {

//    @Bean
//    public CharInterceptor charInterceptor() {
//        return new CharInterceptor();
//    }
//
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        //非法字符过滤
//        registry.addInterceptor(charInterceptor()).addPathPatterns("/**");
//
//        super.addInterceptors(registry);
//    }
}
