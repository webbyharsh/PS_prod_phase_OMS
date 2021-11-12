package com.ps.oms.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "COPY", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .allowedOrigins("http://localhost:3000", "http://44.196.252.191", "https://omssapient.com", "https://oms-603242412.us-east-1.elb.amazonaws.com");
            }
        };

    }
}
