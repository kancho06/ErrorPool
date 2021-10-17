package com.sparta.errorpool.user.webConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${image.upload.directory}")
    private String uploadDir;

    @Value("${image.upload.url}")
    private String uploadUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadUrl+"*")
                .addResourceLocations("file:"+uploadDir);
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        //프론트랑 합칠때 localhost:3000 으로 해야한대서
//                        .allowedOrigins("http://localhost:8080", "http://localhost:3000", "http://errorpool.s3-website.ap-northeast-2.amazonaws.com/")
//                        .maxAge(3000)
//                        .allowedMethods(
//                                HttpMethod.GET.name(),
//                                HttpMethod.HEAD.name(),
//                                HttpMethod.POST.name(),
//                                HttpMethod.PUT.name(),
//                                HttpMethod.DELETE.name());
//            }
//        };
//    }

}