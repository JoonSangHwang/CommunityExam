package com.joonsang.example.CommunityExam.config;

import com.joonsang.example.CommunityExam.listener.SessionListener;
import com.joonsang.example.CommunityExam.resolver.UserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpSessionListener;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserArgumentResolver userArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // HandlerMethodArgumentResolver 추가
        argumentResolvers.add(userArgumentResolver);
    }

    @Bean
    public HttpSessionListener httpSessionListener(){
        return new SessionListener();

    }
}