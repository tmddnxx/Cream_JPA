package com.example.cream_jpa.member.service;

import com.example.cream_jpa.kream.dto.MyProductDTO;
import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.kream.entity.Product;
import com.example.cream_jpa.kream.repository.ProductRepository;
import com.example.cream_jpa.kream.repository.PurchaseBidRepository;
import com.example.cream_jpa.kream.repository.SalesBidRepository;
import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.entity.Member;
import com.example.cream_jpa.member.repository.MemberRepository;
import com.example.cream_jpa.myPage.dto.MySearchDTO;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final PurchaseBidRepository purchaseBidRepository;
    private final SalesBidRepository salesBidRepository;

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
