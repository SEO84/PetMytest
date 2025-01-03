package com.busanit501.bootproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
// 기본 정적 static 경로가 따로 설정, 경로 알려주기,
public class CustomServletConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 웹브라우저에서, http://localhost:8080/js/test.js 연결 시도.
        // 프로젝트 폴더의 /static/js/ 연결 시켜주는 기능.
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
