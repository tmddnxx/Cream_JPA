package com.example.cream_jpa.member.controller;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private  final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public void signUp(){

    }

    @PostMapping("/signup")
    public String signUpPost(MemberDTO memberDTO){

        memberDTO.setPasswd(passwordEncoder.encode(memberDTO.getPasswd()));
        memberDTO.setRole("user");
        memberService.signUp(memberDTO);
        log.info("memberDTO ? "+memberDTO);

        return "redirect:/kream";
    }

    @GetMapping("/kakaoSignup") // 카카오 로그인 후 추가 닉네임, 비밀번호 받기
    public void kakaoSignUp(MemberDTO memberDTO){

    }

}
