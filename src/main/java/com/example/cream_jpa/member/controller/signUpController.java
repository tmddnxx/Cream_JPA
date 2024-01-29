package com.example.cream_jpa.member.controller;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/validation")
@RequiredArgsConstructor
public class signUpController {

    private final MemberService memberService;

    @PostMapping("/memberId/{memberId}")
    public String checkMemberId(@PathVariable("memberId") String memberId){

        MemberDTO memberDTO = memberService.getMemberByMemberId(memberId);

        String msg;

        if (memberDTO != null){
            msg = "이미 사용중인 아이디입니다.";
        }else {
            msg = "";
        }
        return msg;
    }

    @PostMapping("/email/{email}")
    public String checkEmail(@PathVariable("email") String email){
        MemberDTO memberDTO = memberService.findMemberByEmail(email);

        String msg;
        if (memberDTO != null){
            msg = "이미 사용중인 이메일입니다.";
        }else{
            msg = "";
        }

        return msg;
    }
}
