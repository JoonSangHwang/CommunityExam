package com.joonsang.example.CommunityExam.config;

import com.joonsang.example.CommunityExam.ouath.CustomOAuth2Provider;
import com.joonsang.example.CommunityExam.ouath.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.joonsang.example.CommunityExam.user.SocialType.*;

@Configuration
@EnableWebSecurity      // 시큐리티 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
                .authorizeRequests()
                .antMatchers("/",
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
                .formLogin()
                .successForwardUrl("/board/list")
        .and()
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