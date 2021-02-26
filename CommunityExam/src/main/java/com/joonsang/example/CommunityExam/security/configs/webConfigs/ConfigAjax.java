package com.joonsang.example.CommunityExam.security.configs.webConfigs;

import com.joonsang.example.CommunityExam.security.methodAjax.handler.CustomAjaxFailureHandler;
import com.joonsang.example.CommunityExam.security.methodAjax.handler.CustomAjaxSuccessHandler;
import com.joonsang.example.CommunityExam.security.methodAjax.provider.CustomAjaxProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class ConfigAjax {

    @Bean
    @Qualifier("customAjaxSuccessHandler")
    public AuthenticationSuccessHandler customAjaxSuccessHandler(){
        return new CustomAjaxSuccessHandler();
    }

    @Bean
    @Qualifier("customAjaxFailureHandler")
    public AuthenticationFailureHandler customAjaxFailureHandler(){
        return new CustomAjaxFailureHandler();
    }

    @Bean
    @Qualifier("customAjaxProvider")
    public AuthenticationProvider customAjaxProvider() {
        return new CustomAjaxProvider();
    }




}
