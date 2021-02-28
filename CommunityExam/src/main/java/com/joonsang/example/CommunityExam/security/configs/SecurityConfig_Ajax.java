package com.joonsang.example.CommunityExam.security.configs;

import com.joonsang.example.CommunityExam.security.methodAjax.filter.CustomAjaxProcessingFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j
@Order(0)
public class SecurityConfig_Ajax extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("customAjaxProvider")
    private AuthenticationProvider customAjaxProvider;

    @Autowired
    @Qualifier("customAjaxSuccessHandler")
    private AuthenticationSuccessHandler customAjaxSuccessHandler;

    @Autowired
    @Qualifier("customAjaxFailureHandler")
    private AuthenticationFailureHandler customAjaxFailureHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**").authorizeRequests()
                .anyRequest().authenticated()

        .and()
                .addFilterBefore(customAjaxProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        http
                .csrf().disable();
    }

    /**
     * AuthenticationManagerBuilder 를 통해 인증 객체를 만들 수 있도록 제공
     *
     * - AuthenticationProvider 설정
     * - Security 인증 시, AuthenticationManager 가 인증을 AuthenticationProvider 에게 위임
     * - AuthenticationProvider 는 인증 시, CustomFormProvider 를 참조하도록 설정 함
     * - CustomFormProvider 는 DB 에서 사용자를 조회하여 인증을 진행하려는 목적
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAjaxProvider);
    }



    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public CustomAjaxProcessingFilter customAjaxProcessingFilter() throws Exception {
        CustomAjaxProcessingFilter customAjaxProcessingFilter = new CustomAjaxProcessingFilter();
        customAjaxProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        customAjaxProcessingFilter.setAuthenticationSuccessHandler(customAjaxSuccessHandler);  // 성공 핸들러
        customAjaxProcessingFilter.setAuthenticationFailureHandler(customAjaxFailureHandler);  // 실패 핸들러
        return customAjaxProcessingFilter;
    }






}
