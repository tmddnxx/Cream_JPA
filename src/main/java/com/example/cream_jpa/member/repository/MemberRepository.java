package com.example.cream_jpa.member.repository;

import com.example.cream_jpa.kream.repository.queryDSL.QueryRepository;
import com.example.cream_jpa.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByMemberId(String memberId); // 아이디로 회원찾기 (Optional 사용 이유 = null값 방지)

    Optional<Member> findByEmail(String email); // 이메일로 회원찾기

}
