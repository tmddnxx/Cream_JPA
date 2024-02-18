package com.example.cream_jpa.member.controller;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.service.MemberService;
import com.example.cream_jpa.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private  final MemberService memberService;
    private final CustomUserDetailService customUserDetailService;
    @GetMapping("/signup")
    public void signUp(){

    }

    @PostMapping("/signup")
    public String signUpPost(MemberDTO memberDTO){

        memberDTO.setPasswd(customUserDetailService.passwordEncoder().encode(memberDTO.getPasswd()));
        memberDTO.setRole("user");
        memberService.signUp(memberDTO);
        log.info("memberDTO ? "+memberDTO);

        return "redirect:/kream";
    }

    @GetMapping("/kakaoSignup") // 카카오 로그인 후 추가 닉네임, 전화번호, 비밀번호 받기
    public void kakaoSignUp(MemberDTO memberDTO){

    }
    
    @GetMapping("quitHome")
    public void quitHome(){
        // 회원탈퇴 후 페이지
    }

    @GetMapping("/findId")
    public void findId(){
        // 아이디 찾기 페이지
    }

    @PostMapping("/findId") // 전화번호로 인증문자 날려서 아이디찾기
    public String foundId(@RequestParam("phone")String phone, RedirectAttributes redirectAttributes){
        log.info("아이디찾기 ? "+phone);
        MemberDTO memberDTO = memberService.findByPhone(phone);
        String memberId;
        if (memberDTO != null){
            memberId = memberDTO.getMemberId();
            redirectAttributes.addAttribute("memberId", memberId);
        }else{
            memberId = "";
            redirectAttributes.addAttribute("memberId", memberId);
        }
        return "redirect:/member/successFindId";
    }

    @GetMapping("/successFindId") // 찾은 아이디보여주는 페이지
    public void successFindId(@ModelAttribute("memberId") String memberId){
        log.info("성공? "+memberId);
    }
    @GetMapping("/findPw")
    public void findPw(){
        // 비밀번호 찾기 페이지
    }


}
