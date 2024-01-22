package com.example.cream_jpa.member.dto;

import com.example.cream_jpa.member.entity.Member;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO implements UserDetails, OAuth2User {

    private Long mno;
    private String memberId;
    private String passwd;
    private String nickname;
    private String email;
    private String role;

    private List<SimpleGrantedAuthority> authorities;

    Map<String, Object> oAuth2Attributes; // oAuth2User

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
        Member member = Member.builder()
                .mno(this.mno)
                .memberId(this.memberId)
                .passwd(this.passwd)
                .nickname(this.nickname)
                .email(this.email)
                .role(this.role)
                .build();

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

    // 소셜로그인 필수 메서드
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Attributes;
    }
    @Override
    public String getName() {
        return memberId;
    }
}
