package com.hantong.config;


import com.hantong.util.YjUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {
    @Value("${app.release.path}")
    String appReleasePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (this.appReleasePath == null || this.appReleasePath.length() ==0){
            this.appReleasePath = YjUtil.getRootPath()+"/appVersion/release/";
        }

        System.out.println("appReleasePath:"+this.appReleasePath);

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/app-release/**")
                //.addResourceLocations("file:E:/Code/demo2/appVersion");
                .addResourceLocations("file:"+this.appReleasePath);
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}