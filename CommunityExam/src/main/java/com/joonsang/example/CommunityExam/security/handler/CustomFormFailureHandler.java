package com.joonsang.example.CommunityExam.security.handler;

import com.joonsang.example.CommunityExam.exception.UserIdNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomFormFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "";

        if(exception instanceof BadCredentialsException) {
            errorMessage = "Invalid Username or Password";
        } else if(exception instanceof InsufficientAuthenticationException) {
            errorMessage = "Invalid Secreat Key";
        } else if(exception instanceof DisabledException) {
            errorMessage = "Locked";
        } else if(exception instanceof CredentialsExpiredException) {
            errorMessage = "Expired password";
        } else {
            errorMessage = "No";
        }

        setDefaultFailureUrl("/loginPage?error=true&exception=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}
