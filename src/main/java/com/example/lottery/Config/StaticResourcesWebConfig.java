//package com.example.lottery.Config;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class StaticResourcesWebConfig implements WebMvcConfigurer {
//
//    @Value("${hct14.upload-file.base-uri}")
//    private String baseURI;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/storage/**")
//                .addResourceLocations(baseURI);
//    }
//}
