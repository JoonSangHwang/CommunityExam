package com.joonsang.example.CommunityExam.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomFormSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * RequestCache 인터페이스
     * - 구현 : HttpSessionRequestCache
     * - 역할 : DefaultSavedRequest 객체를 세션에 저장하는 역할
     */
    private final RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * RedirectStrategy 인터페이스
     * 구현 : DefaultSavedRequest
     * 역할 : 현재 클라이언트의 요청과정 중에 포함된 쿠키, 헤더, 파라미터 값들을 추출하여 보관하는 역할.
     *       HttpSessionRequestCache 에 의해 세션에 저장된다.
     */
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 로그인 성공 시, 핸들러
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 기본 타겟
        setDefaultTargetUrl("/mainPage");

        // savedRequest 가 null 이라면, 인가처리 필터(FilterSecurityInterceptor) 에서 Exception 이 발생하지 않았다는 의미
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }

        // 그 전... 인증 과정에서 Exception 이 발생하였고 재인증 과정에서 성공한 상태, 세션에서 request URL 을 조회하여 리다이렉트
        String targetUrl = savedRequest.getRedirectUrl();
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}
