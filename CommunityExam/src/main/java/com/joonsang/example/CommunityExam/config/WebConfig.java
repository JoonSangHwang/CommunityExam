package com.joonsang.example.CommunityExam.config;

import com.joonsang.example.CommunityExam.listener.SessionListener;
import com.joonsang.example.CommunityExam.security.methodOAuth2.resolver.UserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    /**
     * OAuth2
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // HandlerMethodArgumentResolver 추가
        argumentResolvers.add(userArgumentResolver);
    }


    /**
     * Bean ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    /**
     * Set Session Timeout
     */
    @Bean
    public HttpSessionListener httpSessionListener(){
        return new SessionListener();
    }






}