package com.example.cream_jpa.member.entity;

import com.example.cream_jpa.member.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_incresement
    private Long mno; // PK값

    @Column(nullable = false, unique = true)
    private String memberId; // 아이디

    @Column(nullable = false)
    private String passwd; // 비밀번호

    @Column(nullable = false)
    private String nickname; // 닉네임

    @Column(nullable = false)
    private String email; // 이메일

    @Column(columnDefinition = "VARCHAR(255) default 'user'")
    private String role = "user"; // 역할

    public MemberDTO toDTO(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMno(this.mno);
        memberDTO.setMemberId(this.memberId);
        memberDTO.setPasswd(this.passwd);
        memberDTO.setNickname(this.nickname);
        memberDTO.setEmail(this.email);
        memberDTO.setRole(this.role);

        return memberDTO;
    }

    public void changePw(String passwd) {
        this.passwd = passwd;
    }
    public void changeNickName(String nickname){
        this.nickname = nickname;
    }

    public void changeEmail(String email){
        this.email = email;
    }
}
