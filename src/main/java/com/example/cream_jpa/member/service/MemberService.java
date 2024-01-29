package com.example.cream_jpa.member.service;

import com.example.cream_jpa.kream.dto.MyProductDTO;
import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.entity.Member;
import com.example.cream_jpa.myPage.dto.MySearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberService {

    void signUp(MemberDTO memberDTO); // 회원가입

    MemberDTO getMemberByMemberId(String memberId); // 아이디로 회원찾기
    MemberDTO findMemberByEmail(String email); // 이메일로 회원찾기
    
    MemberDTO findByMno(Long mno); // mno로 회원찾기

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
