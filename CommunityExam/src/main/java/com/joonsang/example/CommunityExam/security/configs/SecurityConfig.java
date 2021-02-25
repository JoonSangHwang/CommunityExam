package com.joonsang.example.CommunityExam.security.configs;

import com.joonsang.example.CommunityExam.ouath.CustomOAuth2Provider;
import com.joonsang.example.CommunityExam.ouath.CustomOAuth2UserService;
import com.joonsang.example.CommunityExam.security.provider.CustomFormProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.joonsang.example.CommunityExam.entity.enumType.roleType.*;

@Configuration
@EnableWebSecurity
@Slf4j
@Order(0)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private AuthenticationDetailsSource customAuthenticationDetailsSource;

    @Autowired
    private AuthenticationSuccessHandler customFormSuccessHandler;          // 로그인 성공 후, 핸들러

    @Autowired
    private AuthenticationFailureHandler customFormFailureHandler;          // 로그인 실패 후, 핸들러

    @Autowired
    AuthenticationProvider customFormProvider;

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
                .authorizeRequests()
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
                .anyRequest().authenticated();


        http
//                // OAuth 2.0 로그인
//                .oauth2Login()
//                .defaultSuccessUrl("/loginSuccess")         // 로그인 성공 시, 이동 할 URL
//                .failureUrl("/loginFailure")                // 로그인 실패 시, 이동 할 URL
//                .userInfoEndpoint()                         // 로그인 성공 후, 로그인 기능에 대한 여러 설정의 진입점
//                .userService(customOAuth2UserService)       // 로그인 성공 후, 후속 조치 UserService 인터페이스 구현체 [리소스 서버에서 받아온 사용자 정보를 핸들링]
//        .and()
//                .headers().frameOptions().disable()         // H2-console 화면을 사용하기 위해 해당 옵션은 disable()
//        .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
//        .and()
                // Form 로그인
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/loginForm")                                   // Form 태그의 Action URL
                .authenticationDetailsSource(customAuthenticationDetailsSource)     // username, password 이외에 추가 파라미터 처리
                .defaultSuccessUrl("/mainPage")                                     // 인증 성공 시, 이동 URL
                .successHandler(customFormSuccessHandler)                           // 로그인 성공 후, 핸들러
                .failureHandler(customFormFailureHandler)                           // 로그인 실패 후, 핸들러
//                .successForwardUrl("/mainPage")
//        .and()
//                // 로그아웃
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/")          // 로그아웃 성공 시, 이동 할 URL
//                .deleteCookies("JSESSIONID")    // 쿠키 삭제
//                .invalidateHttpSession(true)
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




    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oAuth2ClientProperties, @Value("${spring.security.oauth2.client.registration.kakao.clientId}") String kakaoClientId) {

        // getRegistration() 메소드를 사용해 구글과 페이스북의 인증 정보를 빌드
        List<ClientRegistration> registrations = oAuth2ClientProperties.getRegistration().keySet().stream()
                .map(client -> getRegistration(oAuth2ClientProperties, client))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 카카오 인증 정보 추가. 실제 요청 시, clientId 만 필요하지만 clientSecret 와 jwtSetUri 가 null 이면 에러나므로 임시 값 부여.
        registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                .clientId(kakaoClientId)    // 필수 값
                .clientSecret("temp")       // 필요 없는 값
                .jwkSetUri("temp")          // 필요 없는 값
                .build());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client) {
        if ("google".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email", "profile")
                    .build();
        }
        if ("facebook".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    // 페이스북의 그래프 API 같은 경우... scope() 로는 필요한 필드를 반환해주지 않기 때문에 직접 파라미터에 넣어 요청
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
                    .scope("email")
                    .build();
        }
        return null;
    }
}
