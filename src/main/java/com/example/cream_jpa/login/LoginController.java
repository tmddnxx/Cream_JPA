package com.example.cream_jpa.login;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    @GetMapping("")
    public String login(HttpSession session, Model model){
        log.info("로그인페이지");

        String failLogin = (String) session.getAttribute("failLogin");

        if (failLogin != null){
            model.addAttribute("failLogin", failLogin);
        }

        return "login/login";
    }

}
