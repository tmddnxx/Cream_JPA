package com.example.cream_jpa.member.service;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.entity.Member;

import java.util.Optional;

public interface MemberService {

    void signUp(MemberDTO memberDTO); // 회원가입

    Optional<Member> getMemberByMemberId(String memberId); // 아이디로 회원찾기
}
