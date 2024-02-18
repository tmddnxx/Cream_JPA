package com.example.cream_jpa.member.controller;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.service.MemberService;
import com.example.cream_jpa.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PutMapping("/passwd")
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

    @PutMapping("/nickName") // 닉네임변경
    public String modifyNickName(@RequestBody MemberDTO memberDTO){
        memberService.changeNick(memberDTO.getMno(), memberDTO.getNickname());
        
        sessionUpdate(memberDTO);

        return memberDTO.getNickname();
    }

    @PutMapping("/email") // 이메일 변경
    public String modifyEmail(@RequestBody MemberDTO memberDTO){
        memberService.changeEmail(memberDTO.getMno(), memberDTO.getEmail());

        sessionUpdate(memberDTO);
        
        return memberDTO.getEmail();
    }

    @PostMapping("/findPw") // 비밀번호 찾기(회원찾기)
    public boolean findPw(@RequestBody MemberDTO memberDTO){
        MemberDTO findMember = memberService.findPw(memberDTO.getMemberId(), memberDTO.getPhone());

        if (findMember != null){
            // 문자로 임시비밀번호 보냄
            return true;
        }else{
            return false;
        }
    }

    private void sessionUpdate(MemberDTO memberDTO){ // 세션업데이트
        // 변경된 회원정보
        UserDetails userDetails = customUserDetailService.loadUserByUsername(memberDTO.getMemberId());
        // 회원인증하기
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // 세션 재설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
