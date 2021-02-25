package com.joonsang.example.CommunityExam.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomFormAccessDeniedHandler implements AccessDeniedHandler {

    private String errorPage;

    /**
     * 인가할 수 없다는 메시지와 URL 을 뿌려줌
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String deiniedUrl = errorPage + "?exception=" + accessDeniedException.getMessage();
        response.sendRedirect(deiniedUrl);
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }
}
