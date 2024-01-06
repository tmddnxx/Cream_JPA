package com.example.cream_jpa.member.dto;

import com.example.cream_jpa.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {

    private Long mno;
    private String memberId;
    private String passwd;
    private String nickname;
    private String email;
    private String role;


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

}
