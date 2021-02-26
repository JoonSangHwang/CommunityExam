package com.joonsang.example.CommunityExam.security.configs;

import com.joonsang.example.CommunityExam.security.methodOAuth2.service.CustomOAuth2UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.joonsang.example.CommunityExam.entity.enumType.roleType.*;

@Configuration
@EnableWebSecurity
@Slf4j
@Order(1)
public class SecurityConfig_OAuth2 extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;                // OAuth2.0



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http

                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/oauth2/**",
                        "/login/**",
                        "/css/**",
                        "/images/**",
                        "/js/**",
                        "/console/**").permitAll()
//                .antMatchers("/mainPage").permitAll()           // [페이지] 메인
                .antMatchers("/facebook").hasAuthority(FACEBOOK.getRoleType())
                .antMatchers("/google").hasAuthority(GOOGLE.getRoleType())
                .antMatchers("/kakao").hasAuthority(KAKAO.getRoleType())
//                .anyRequest().authenticated()
        .and()
                // OAuth 2.0 로그인
                .oauth2Login()
                .defaultSuccessUrl("/loginSuccess")         // 로그인 성공 시, 이동 할 URL
                .failureUrl("/loginFailure")                // 로그인 실패 시, 이동 할 URL
                .userInfoEndpoint()                         // 로그인 성공 후, 로그인 기능에 대한 여러 설정의 진입점
                .userService(customOAuth2UserService)       // 로그인 성공 후, 후속 조치 UserService 인터페이스 구현체 [리소스 서버에서 받아온 사용자 정보를 핸들링]
        .and()
//                .headers().frameOptions().disable()         // H2-console 화면을 사용하기 위해 해당 옵션은 disable()
        .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        .and()
                // Form 로그인
//                .formLogin()
//                .successForwardUrl("/board/list")
//                .and()
                // 로그아웃
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")          // 로그아웃 성공 시, 이동 할 URL
                .deleteCookies("JSESSIONID")    // 쿠키 삭제
                .invalidateHttpSession(true)
        .and()
                .addFilterBefore(filter, CsrfFilter.class)
                .csrf().disable();
    }

}
