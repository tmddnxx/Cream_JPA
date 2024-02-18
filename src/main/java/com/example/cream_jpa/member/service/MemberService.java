package com.example.cream_jpa.member.service;

import com.example.cream_jpa.cream.dto.MyProductDTO;
import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.myPage.dto.MySearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {

    void signUp(MemberDTO memberDTO); // 회원가입

    MemberDTO getMemberByMemberId(String memberId); // 아이디로 회원찾기
    MemberDTO findMemberByEmail(String email); // 이메일로 회원찾기
    MemberDTO findByMno(Long mno); // mno로 회원찾기
    MemberDTO findByPhone(String phone); //아이디 찾기
    
    MemberDTO findPw(String memberId, String phone); // 비밀번호 찾기
    void changePw(Long mno, String passwd); // 비밀번호 변경
    void changeNick(Long mno, String nickName); // 닉네임 변경
    void changeEmail(Long mno, String email); // 이메일 변경
    void withdrawal(Long mno); // 회원탈퇴
    int countAllPurchaseBidByMno(Long mno); // 특정 회원 구매입찰 전체 찾기
    int countPurchaseBidIsBuyFalse(Long mno); // 특정 회원 구매입찰 진행중 찾기
    int countPurchaseBidIsBuyTrue(Long mno); // 특정 회원 구매입찰 진행중 찾기
    List<MyProductDTO> purchaseTop3List(Long mno); // 마이페이지 구매입찰내역 3개 List

    int countAllSalesBidByMno(Long mno); // 특정 회원 판매입찰 전체 찾기
    int countSalesBidIsBuyFalse(Long mno); // 특정 회원 판매입찰 진행중 찾기
    int countSalesBidIsBuyTrue(Long mno); // 특정 회원 판매입찰 진행중 찾기
    List<MyProductDTO> salesTop3List(Long mno); // 마이페이지 판매입찰내역 3개 List
    
    Page<MyProductDTO> allPurchase_lt6(MySearchDTO mySearchDTO, Pageable pageable, Long mno); // 특정회원 모든 구매입찰내역(6개월전)
    Page<MyProductDTO> allSales_lt6(MySearchDTO mySearchDTO, Pageable pageable, Long mno); // 특정회원 모든 판매입찰내역(6개월전)
}
