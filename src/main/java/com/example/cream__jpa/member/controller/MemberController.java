package com.example.cream_jpa.member.controller;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private  final MemberService memberService;


    @GetMapping("/signup")
    public void signUp(){

    }

    @PostMapping("/signup")
    public String signUpPost(MemberDTO memberDTO){

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDTO.setRole("user");
        memberDTO.setPasswd(passwordEncoder.encode(memberDTO.getPasswd()));
        memberService.signUp(memberDTO);
        log.info("memberDTO ? "+memberDTO);

        return "redirect:/kream";
    }

}
