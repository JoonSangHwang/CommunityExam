package com.joonsang.example.CommunityExam.config;

import com.joonsang.example.CommunityExam.listener.SessionListener;
import com.joonsang.example.CommunityExam.ouath.resolver.UserArgumentResolver;
import com.joonsang.example.CommunityExam.security.common.CustomAuthenticationDetailsSource;
import com.joonsang.example.CommunityExam.security.provider.CustomFormProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserArgumentResolver userArgumentResolver;

    /**
     *
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


    /**
     * Bean AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomFormProvider(passwordEncoder());
    }

    @Bean
    public AuthenticationDetailsSource authenticationDetailsSource() {
        return new CustomAuthenticationDetailsSource();
    }


    /**
     * Password Encryption Processing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Spring Security 5 이전, NoOp 전략 [deprecated]
//		return NoOpPasswordEncoder.getInstance();

        // Spring Security 5 이후, bcrypt 전략
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // Custom
        String idForEncode = "bcrypt";
        Map encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("sha256", new StandardPasswordEncoder());

        PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
        return passwordEncoder;
    }

}