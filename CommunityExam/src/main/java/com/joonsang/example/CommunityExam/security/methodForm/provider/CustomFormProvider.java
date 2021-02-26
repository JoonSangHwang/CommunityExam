package com.joonsang.example.CommunityExam.security.methodForm.provider;

import com.joonsang.example.CommunityExam.security.common.AccountContext;
import com.joonsang.example.CommunityExam.security.common.CustomWebAuthenticationDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

@Slf4j
public class CustomFormProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService customFormUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomFormProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 인증
     */
    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // Param 을 username 으로 해줌으로서 가져오고 있는 상태.... 왜일까?
        String username = authentication.getName();                     // 아이디
        String password = (String) authentication.getCredentials();     // 패스워드

        // ID 검증 - 커스터마이징한 loadUserByUsername()
        AccountContext accountContext = (AccountContext) customFormUserDetailsService.loadUserByUsername(username);

        // 패스워드 검증 - 입력한 패스워드와 암호화 된 패스워드 일치 검증
        if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        // SecretKey 검증 - details 에 저장 되어 있음
        CustomWebAuthenticationDetails customWebAuthenticationDetails = (CustomWebAuthenticationDetails) authentication.getDetails();
        String secretKey = customWebAuthenticationDetails.getSecretKey();
        if (secretKey == null || !"준상".equals(secretKey)) {
            throw new InsufficientAuthenticationException("Invalid Secret");
        }

        /*** 위 검증을 다 완료할 경우, Provider 객체는 토큰을 생성한다. ***/

        // 토큰 생성 (파라미터: 객체정보/패스워드/권한정보)
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());

        // AuthenticationManager 에게 인증 된 정보 전달
        //
        return usernamePasswordAuthenticationToken;
    }

    /**
     * Authentication 객체 타입과 UsernamePasswordAuthenticationToken 객체 타입이 같은지 확인
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
