package com.example.cream_jpa.member.service;

import com.example.cream_jpa.cream.dto.MyProductDTO;
import com.example.cream_jpa.cream.repository.ProductRepository;
import com.example.cream_jpa.cream.repository.PurchaseBidRepository;
import com.example.cream_jpa.cream.repository.SalesBidRepository;
import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.entity.Member;
import com.example.cream_jpa.member.repository.MemberRepository;
import com.example.cream_jpa.myPage.dto.MySearchDTO;
import com.example.cream_jpa.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final PurchaseBidRepository purchaseBidRepository;
    private final SalesBidRepository salesBidRepository;

    private final CustomUserDetailService customUserDetailService;
    @Override
    public void signUp(MemberDTO memberDTO) { // 회원가입
        Member member = memberDTO.toEntity();

        memberRepository.save(member);
    }

    @Override
    public MemberDTO getMemberByMemberId(String memberId) { // ID로 회원찾기
        Optional<Member> member = memberRepository.findByMemberId(memberId);

        return member.map(Member::toDTO).orElse(null);
    }

    @Override
    public MemberDTO findMemberByEmail(String email) { // 이메일로 회원찾기
        Optional<Member> member = memberRepository.findByEmail(email);

        return member.map(Member::toDTO).orElse(null);
    }

    @Override
    public MemberDTO findByMno(Long mno) { // mno로 회원찾기

        Optional<Member> member = memberRepository.findById(mno);
        MemberDTO memberDTO;
        memberDTO = member.map(Member::toDTO).orElse(null);

        return memberDTO;
    }

    @Override
    public MemberDTO findByPhone(String phone) { // 아이디찾기
        Optional<Member> member = memberRepository.findByPhone(phone);
        MemberDTO memberDTO;
        memberDTO = member.map(Member::toDTO).orElse(null);
        return memberDTO;
    }

    @Override // 비밀번호 찾기
    public MemberDTO findPw(String memberId, String phone) {
        Optional<Member> member = memberRepository.findMemberByMemberIdAndPhone(memberId, phone);
        MemberDTO memberDTO;
        memberDTO = member.map(Member::toDTO).orElse(null);
        return memberDTO;
    }

    @Override // 비밀번호 변경
    public void changePw(Long mno, String passwd) {
        Optional<Member> member = memberRepository.findById(mno);
        Member actualMember = member.get();

        actualMember.changePw(customUserDetailService.passwordEncoder().encode(passwd));
        memberRepository.save(actualMember);
    }

    @Override
    public void changeNick(Long mno, String nickName) {
        Optional<Member> member = memberRepository.findById(mno);
        Member actualMember = member.get();

        actualMember.changeNickName(nickName);
        memberRepository.save(actualMember);
    }

    @Override
    public void changeEmail(Long mno, String email) {
        Optional<Member> member = memberRepository.findById(mno);
        Member actualMember = member.get();

        actualMember.changeEmail(email);
        memberRepository.save(actualMember);
    }

    @Override
    public void withdrawal(Long mno) {
        memberRepository.deleteById(mno);
    }

    @Override
    public int countAllPurchaseBidByMno(Long mno) {

        return purchaseBidRepository.countPurchase_bidByMno(mno);
    }

    @Override
    public int countPurchaseBidIsBuyFalse(Long mno) {

        return purchaseBidRepository.countPurchase_bidByMnoAndIsBuyFalse(mno);
    }

    @Override
    public int countPurchaseBidIsBuyTrue(Long mno) {

        return purchaseBidRepository.countPurchase_bidByMnoAndIsBuyTrue(mno);
    }

    @Override
    public List<MyProductDTO> purchaseTop3List(Long mno) {

        List<MyProductDTO> productDTOList = productRepository.purchaseListTop3(mno);

        return productDTOList;
    }

    @Override
    public int countAllSalesBidByMno(Long mno) {
        return salesBidRepository.countSales_bidByMno(mno);
    }

    @Override
    public int countSalesBidIsBuyFalse(Long mno) {
        return salesBidRepository.countSales_bidByMnoAndIsBuyFalse(mno);
    }

    @Override
    public int countSalesBidIsBuyTrue(Long mno) {
        return salesBidRepository.countSales_bidByMnoAndIsBuyTrue(mno);
    }

    @Override
    public List<MyProductDTO> salesTop3List(Long mno) {
        return productRepository.salesListTop3(mno);
    }

    @Override
    public Page<MyProductDTO> allPurchase_lt6(MySearchDTO mySearchDTO, Pageable pageable, Long mno) {

        pageable = PageRequest.of(Math.max(0, pageable.getPageNumber() -1), 5);

        return productRepository.allPurchaseList_lt6(mySearchDTO, pageable, mno);
    }

    @Override
    public Page<MyProductDTO> allSales_lt6(MySearchDTO mySearchDTO, Pageable pageable, Long mno) {
        pageable = PageRequest.of(Math.max(0, pageable.getPageNumber() -1), 5);

        return productRepository.allSalesList_lt6(mySearchDTO, pageable, mno);
    }

}
