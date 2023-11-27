package com.fmm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/landing-page").setViewName("landing-page");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registration").setViewName("registration");

        registry.addViewController("/fmm/my-profile").setViewName("my-profile");
        registry.addViewController("/fmm/opponent-profile").setViewName("opponent-profile");
        registry.addViewController("/fmm/players").setViewName("players");
        registry.addViewController("/fmm/messages").setViewName("messages");
    }
}
