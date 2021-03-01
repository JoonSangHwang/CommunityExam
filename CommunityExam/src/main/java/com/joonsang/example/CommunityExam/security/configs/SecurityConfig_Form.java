package com.joonsang.example.CommunityExam.security.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.joonsang.example.CommunityExam.entity.enumType.roleType.*;

@Configuration
@EnableWebSecurity      // 시큐리티 활성화
@Slf4j
@Order(2)
public class SecurityConfig_Form extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationDetailsSource customAuthenticationDetailsSource;  // id/pw 외 추가 파라미터를 받기 위한 겍체

    @Autowired
    private AuthenticationSuccessHandler customFormSuccessHandler;          // 로그인 성공 후, 핸들러

    @Autowired
    private AuthenticationFailureHandler customFormFailureHandler;          // 로그인 실패 후, 핸들러

    @Autowired
    private AccessDeniedHandler customFormAccessDeniedHandler;              // 인가 거부 처리, 핸들러

    @Autowired
    @Qualifier("customFormProvider")
    private AuthenticationProvider customFormProvider;                      // 인증 Provider

    private static final String[] AUTH_WHITELIST = {
            "/docs/**",
            "/csrf/**",
            "/webjars/**",
            "/**swagger**/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/v2/api-docs"
    };


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        http
                .addFilterBefore(filter, CsrfFilter.class)                 // 커스텀 필터 추가 (Default: UsernamePasswordAuthenticationFilter 보다 먼저 실행된다)
                .csrf().disable();

        http
                .authorizeRequests()                                       // 요청에 대한 권한을 지정
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/").permitAll()                   // [페이지] index
                .antMatchers("/mainPage").permitAll()           // [페이지] 메인
                .antMatchers("/loginPage").permitAll()          // [페이지] 로그인
                .antMatchers("/signUpPage").permitAll()         // [페이지] 회원가입
                .antMatchers("/signUp").permitAll()             // [페이지] 회원가입

                .antMatchers("/loginPage*").permitAll()         // [컨트롤러]

                .antMatchers("/mypage").hasRole("BRONZE")
                .antMatchers("/messages").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")
                .antMatchers(
                        "/",
                        "/oauth2/**",
                        "/login/**",
                        "/css/**",
                        "/images/**",
                        "/js/**",
                        "/console/**").permitAll()
                .antMatchers("/facebook").hasAuthority(FACEBOOK.getRoleType())
                .antMatchers("/google").hasAuthority(GOOGLE.getRoleType())
                .antMatchers("/kakao").hasAuthority(KAKAO.getRoleType())
                .anyRequest().authenticated()
        ;


        http
                // Form 로그인
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/loginForm")                                   // Form 태그의 Action URL
                .authenticationDetailsSource(customAuthenticationDetailsSource)     // username, password 이외에 추가 파라미터 처리
                .defaultSuccessUrl("/mainPage")                                     // 인증 성공 시, 이동 URL
                .successHandler(customFormSuccessHandler)                           // 로그인 성공 후, 핸들러
                .failureHandler(customFormFailureHandler)                           // 로그인 실패 후, 핸들러
        ;
        http
                .exceptionHandling()                                                // 예외 핸들러
                .accessDeniedHandler(customFormAccessDeniedHandler)                 // 인가 거부 처리 (권한을 가지지 않은 사용자가 페이지에 접근)
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .and()
                // 로그아웃
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")          // 로그아웃 성공 시, 이동 할 URL
                .deleteCookies("JSESSIONID")    // 쿠키 삭제
                .invalidateHttpSession(true)
        ;
    }



    /**
     * ignoring 설정
     *
     * - 정적 자원 관리
     * - StaticResourceLocation (CSS / JAVA_SCRIPT / IMAGES / WEB_JARS / favicon 등) 객체는 보안 필터를 안거치도록 설정
     *
     * 참고 : permitAll() 같은 메소드는 보안 필터를 거쳐 인증을 받을 필요가 없다고 인가를 받는 것. ignoring() 은 보안필터 자체를 안거침
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 디버그 시, 정적 자원은 필터가 0개 인걸 확인 할 수 있음
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
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
        auth.authenticationProvider(customFormProvider);
    }

}
