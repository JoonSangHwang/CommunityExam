package com.joonsang.example.CommunityExam.security.configs.webConfigs;

import com.joonsang.example.CommunityExam.security.common.CustomAuthenticationDetailsSource;
import com.joonsang.example.CommunityExam.security.methodForm.handler.CustomFormAccessDeniedHandler;
import com.joonsang.example.CommunityExam.security.methodForm.provider.CustomFormProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigForm {

    /**
     * Bean AuthenticationProvider
     */
    @Bean
    @Qualifier("customFormProvider")
    public AuthenticationProvider customFormProvider() {
        return new CustomFormProvider(passwordEncoder());
    }

    @Bean
    public AuthenticationDetailsSource authenticationDetailsSource() {
        return new CustomAuthenticationDetailsSource();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomFormAccessDeniedHandler customFormAccessDeniedHandler = new CustomFormAccessDeniedHandler();
        customFormAccessDeniedHandler.setErrorPage("/denied");
        return customFormAccessDeniedHandler;
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
