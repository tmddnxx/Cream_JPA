package com.example.cream_jpa.sms.controller;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.service.MemberService;
import com.example.cream_jpa.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
@Log4j2
public class SMSController {

    private final String apiKey;
    private final String secretKey;
    private final DefaultMessageService messageService;
    private final MemberService memberService;
    private final CustomUserDetailService customUserDetailService;

    @Autowired
    public SMSController(@Value("${coolsms.api.key}") String apiKey,
                         @Value("${coolsms.api.secret}") String secretKey, MemberService memberService, CustomUserDetailService customUserDetailService) {
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.memberService = memberService;
        this.customUserDetailService = customUserDetailService;
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, "https://api.coolsms.co.kr");
    }

    @PostMapping("/sendOne") // 인증코드보내기
    public StringBuilder sendOne(@RequestParam("toPhone") String toPhone) {
        Message message = new Message();
        Random code = new Random();
        StringBuilder numStr = new StringBuilder();
        log.info("받는 번호 ? "+toPhone);
        for (int i=0; i<6; i++){
            String ran = Integer.toString(code.nextInt(10));
            numStr.append(ran);
        }
        log.info(numStr);
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01068788762"); // 보내는 사람
        message.setTo(toPhone); // 받는 사람
        message.setText("[Cream] \n인증번호는 ["+numStr+"] 입니다"); // 인증코드

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        log.info("메시지 ? "+response);

        return numStr;
    }

    @PostMapping("/sendPasswd") // 임시비밀번호 보내기
    public StringBuilder sendPasswd(@RequestParam("toPhone") String toPhone) {
        Message message = new Message();
        Random random = new Random();
        StringBuilder randomStr = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            if (random.nextBoolean()) {
                // 랜덤 대문자 추가
                char randomChar = (char) ('A' + random.nextInt(26));
                randomStr.append(randomChar);
            } else {
                // 랜덤 숫자 추가
                randomStr.append(random.nextInt(10));
            }
        }

        char[] specialChars = {'!', '@', '#', '$', '%'};
        randomStr.append(specialChars[random.nextInt(specialChars.length)]);

        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01068788762"); // 보내는 사람
        message.setTo(toPhone); // 받는 사람
        message.setText("[Cream] \n임시 비밀번호는 ["+randomStr+"] 입니다"); // 임시비밀번호

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        log.info("메시지 ? "+response);

        MemberDTO memberDTO = memberService.findByPhone(toPhone);
        memberService.changePw(memberDTO.getMno(), String.valueOf(randomStr));
        log.info("임시비밀번호 ? " + randomStr);
        return randomStr;
    }
}
