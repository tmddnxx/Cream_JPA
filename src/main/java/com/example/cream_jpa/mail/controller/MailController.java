package com.example.cream_jpa.mail.controller;

import com.example.cream_jpa.mail.service.MailSenderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
@Log4j2
public class MailController {

    private final MailSenderService mailSenderService;

    @GetMapping("/sendConfirmMail")
    public boolean sendConfirmMail(String mailTo, HttpSession session) throws Exception {
        log.info("sendConfirmMail(GET)...");
        // 이메일 인증코드 전송
        if (mailSenderService.sendMail(mailTo)) {
            session.setAttribute("confirmKey", mailSenderService.getConfirmKey());
            return true;
        }
        return false;
    }

    @PostMapping("/matchConfirmKey")
    public boolean matchConfirmKey(@RequestParam("reqConfirmKey") String reqConfirmKey, HttpSession session) {
        // 이메일 인증코드 확인
        log.info("/matchConfirmKey(POST)...");
        log.info(reqConfirmKey);
        String confirmKey = (String) session.getAttribute("confirmKey");
        log.info(confirmKey);

        return reqConfirmKey.equals(confirmKey);
    }

    @GetMapping("/removeConfirmKey")
    public String removeConfirmKey(HttpSession session) {
        // 세션에 저장된 인증코드 삭제
        session.removeAttribute("confirmKey");

        log.info(session.getAttribute("confirmKey"));

        return "true";
    }
}
