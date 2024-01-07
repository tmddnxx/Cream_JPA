package com.example.cream_jpa.member.service;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.entity.Member;
import com.example.cream_jpa.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public void signUp(MemberDTO memberDTO) { // 회원가입
        Member member = memberDTO.toEntity();

        memberRepository.save(member);
    }

    @Override
    public Optional<Member> getMemberByMemberId(String memberId) { // ID로 회원찾기

        return memberRepository.findByMemberId(memberId);
    }

}
