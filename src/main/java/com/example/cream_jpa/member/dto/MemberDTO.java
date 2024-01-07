package com.example.cream_jpa.member.dto;

import com.example.cream_jpa.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@Setter
@ToString
public class MemberDTO implements UserDetails {

    private Long mno;
    private String memberId;
    private String passwd;
    private String nickname;
    private String email;
    private String role;

    private List<SimpleGrantedAuthority> authorities;

    public MemberDTO(Long mno, String memberId, String passwd, String nickname, String email, String role, List<SimpleGrantedAuthority> authorities) {
        this.mno = mno;
        this.memberId = memberId;
        this.passwd = passwd;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.authorities = authorities;
    }


    // MemberDTO에서 Member 엔티티로 변환
    public Member toEntity() {
        Member member = new Member();
        member.setMno(this.mno);
        member.setMemberId(this.memberId);
        member.setPasswd(this.passwd);
        member.setNickname(this.nickname);
        member.setEmail(this.email);
        member.setRole(this.role);
        return member;
    }

    @Override
    public String getPassword() {
        return passwd;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
