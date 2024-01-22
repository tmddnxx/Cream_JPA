package com.example.cream_jpa.config;

import com.example.cream_jpa.security.CustomUserDetailService;
import com.example.cream_jpa.security.OAuth2UserService;
import com.example.cream_jpa.security.handler.LoginFailureHandler;
import com.example.cream_jpa.security.handler.SocialSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final OAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 인증 필요한 url
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/kream/register").authenticated()
                        .requestMatchers(HttpMethod.POST, "/kream/register").authenticated()
                        .requestMatchers(HttpMethod.GET, "/kream/modify").authenticated()
                        .requestMatchers(HttpMethod.POST, "/kream/modify").authenticated()
                        .requestMatchers(HttpMethod.GET, "/sales").authenticated()
                        .requestMatchers(HttpMethod.POST, "/sales/add").authenticated()
                        .requestMatchers(HttpMethod.GET, "/purchase").authenticated()
                        .requestMatchers(HttpMethod.POST, "/purchase/add").authenticated()
                        .anyRequest()
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())
                // 커스텀 로그인 페이지
                .formLogin(formLogin -> formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/kream")
                                .failureHandler(new LoginFailureHandler())
                                .permitAll()
                        // 카카오로그인
                ).oauth2Login(oauthLogin -> oauthLogin
                        .loginPage("/login")
                                .successHandler(new SocialSuccessHandler())
                        // 로그아웃 페이지
                ).logout(logoutConfig ->
                        logoutConfig
                                .logoutUrl("/logout")
                                .logoutSuccessHandler(
                                        (request, response, authentication) -> {
                                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                            response.sendRedirect("/kream");
                                        })
                                .permitAll()
                                );
        return http.build();
    }


}
