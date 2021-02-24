package com.joonsang.example.CommunityExam.security.common;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 *   id / pw 이외에, 추가 파라미터(ex: OTP 코드)를 처리하기 위한 방법
 *
 *   1. AuthenticationDetailsSource 를 구현 할 WebAuthenticationDetails 객체 생성
 *   2. WebAuthenticationDetails 에 추가 파라미터 정의 (secretKey)
 *      -> 참고로 WebAuthenticationDetails 는 기본적으로 remoteAddress 와 sessionId 를 가지고 있음
 * ┌──────────────────────────────────────────────────────────────────────────────────────────┐
 *   3. 커스터마이징 한 WebAuthenticationDetails 을 buildDetails !!
 *      -> Authentication 객체의 details 에 추가 파라미터 저장
 *      -> 참고로 details 는 Object 타입이므로 어떠한 객체도 저장 가능하다.
 * └──────────────────────────────────────────────────────────────────────────────────────────┘
 *   4. SecurityConfig 설정
 */
public class CustomAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new CustomWebAuthenticationDetails(request);
    }
}
