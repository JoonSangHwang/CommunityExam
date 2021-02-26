package com.joonsang.example.CommunityExam.security.methodAjax.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joonsang.example.CommunityExam.entity.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAjaxSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // CustomAjaxProcessingFilter 에서 생성한 인증 객체를 가져옴
        Account account = (Account) authentication.getPrincipal();

        response.setStatus(HttpStatus.OK.value());                  // 응답값 200
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);  // 미디어타입 JSON

//        HttpSession session = request.getSession();
//        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        // JSON 형식으로 클라이언트에게 반환 됨
        objectMapper.writeValue(response.getWriter(), account);
    }
}
