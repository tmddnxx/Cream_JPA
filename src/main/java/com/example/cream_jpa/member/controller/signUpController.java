package com.example.cream_jpa.member.controller;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.service.MemberService;
import com.example.cream_jpa.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/validation")
@RequiredArgsConstructor
public class signUpController {

    private final MemberService memberService;
    private final CustomUserDetailService customUserDetailService;

    // 아이디 중복체크
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
    
    // 이메일 중복체크
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
    
    // 기존 비밀번호 확인 및 변경
    @PostMapping("/passwd")
    public boolean checkPasswd(@RequestParam("existingPw") String existingPw, @RequestParam("mno") Long mno, @RequestParam("newPw") String newPw){
        MemberDTO memberDTO = memberService.findByMno(mno);
        String dbPasswd = memberDTO.getPasswd();
        // 사용자가 입력한 평문비밀번호와 암호화된 비밀번호 일치확인
        boolean pwCheck = customUserDetailService.passwordEncoder().matches(existingPw, dbPasswd);

        if (pwCheck){
            memberService.changePw(mno, newPw);
            return true;
        }else {
            return false;
        }
    }

    @PostMapping("/nickName") // 닉네임변경
    public String modifyNickName(@RequestBody MemberDTO memberDTO){
        memberService.changeNick(memberDTO.getMno(), memberDTO.getNickname());

        return memberDTO.getNickname();
    }

    @PostMapping("/email") // 이메일 변경
    public String modifyEmail(@RequestBody MemberDTO memberDTO){
        memberService.changeEmail(memberDTO.getMno(), memberDTO.getEmail());

        return memberDTO.getEmail();
    }


}
