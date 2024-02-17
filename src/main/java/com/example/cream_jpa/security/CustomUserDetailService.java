package com.example.cream_jpa.security;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.entity.Member;
import com.example.cream_jpa.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByMemberId(username); // view에서 전달받은 username(memberId)으로 회원 정보찾기

        if(member.isEmpty()){
            throw new UsernameNotFoundException("회원 정보가 없습니다");
        }

        // vo에는 없는 authorities 임의로 생성해서 값 넣어서 memberDTO에 저장
        // 나는 역할을 하나만 사용할 것이기 때문에 기본 값인 user로 넣음
        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));

        // memberDTO에 저장해서 세션에 띄움 // view에서 사용가능함
        Member actualMember = member.get();
        return new MemberDTO(actualMember.getMno(), actualMember.getMemberId(), actualMember.getPasswd(),
                actualMember.getNickname(), actualMember.getEmail(), actualMember.getPhone(), actualMember.getRole(), authorities);

    }



    @Bean // 비밀번호 암호화
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
