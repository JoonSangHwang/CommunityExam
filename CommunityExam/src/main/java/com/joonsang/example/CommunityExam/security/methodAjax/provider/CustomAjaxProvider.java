package com.joonsang.example.CommunityExam.security.methodAjax.provider;

import com.joonsang.example.CommunityExam.security.methodAjax.token.CustomAjaxToken;
import com.joonsang.example.CommunityExam.security.common.AccountContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

/**
 * 인증 검증이므로... Form 인증과 동일한 절차를 가진다.
 * - 토큰 생성 부분만 달리 해준다.
 */
public class CustomAjaxProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 인증 검증
     *
     * @param authentication    AuthenticationManager 가 주는 객체로, 사용자가 입력한 계정 정보가 담겨 있다.
     */
    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 사용자가 입력한 내용 추출
        String username = authentication.getName();                     // 아이디
        String password = (String) authentication.getCredentials();     // 패스워드

        // ID 검증 - 커스터마이징한 loadUserByUsername()
        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

        // 패스워드 검증 - 입력한 패스워드와 암호화 된 패스워드 일치 검증
        if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        /* 위 검증을 다 완료할 경우, Provider 객체는 인증 완료 된 토큰을 생성한다. */

        // 토큰 생성 (파라미터: 인증에 성공한 객체 정보/패스워드/권한정보)
        CustomAjaxToken customAjaxToken = new CustomAjaxToken(accountContext.getAccount(), null, accountContext.getAuthorities());

        // AuthenticationManager 에게 인증 된 토큰 정보 반환
        return customAjaxToken;
    }

    /**
     * 인증 객체가 토큰 타입과 같을 경우, Provider 동작
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomAjaxToken.class);
    }
}