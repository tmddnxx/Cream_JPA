package com.example.cream_jpa.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 실패 메시지를 담기 위해 세션 생성
        HttpSession session = request.getSession();
        // 세션에 실패 메시지 담기
        session.setAttribute("정보가 일치하지 않습니다.", exception.getMessage());
        // 실패 시 이동할 페이지
        setDefaultFailureUrl("/signin");
        super.onAuthenticationFailure(request, response, exception);
    }
}
