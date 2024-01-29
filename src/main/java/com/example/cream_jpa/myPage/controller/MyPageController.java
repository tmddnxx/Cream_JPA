package com.example.cream_jpa.myPage.controller;

import com.example.cream_jpa.kream.dto.MyProductDTO;
import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.entity.Member;
import com.example.cream_jpa.member.service.MemberService;
import com.example.cream_jpa.myPage.dto.MySearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/myPage")
public class MyPageController {

    private final MemberService memberService;

    @GetMapping("")
    private String home(@AuthenticationPrincipal MemberDTO memberDTO, Model model){

        memberDTO = memberService.findByMno(memberDTO.getMno());
        model.addAttribute("memberDTO", memberDTO); // 로그인 유저 회원정보

        // 구매내역 갯수
        countPurchaseBid(model, memberDTO);

        List<MyProductDTO> purchaseList = memberService.purchaseTop3List(memberDTO.getMno());
        model.addAttribute("purchaseList", purchaseList); // 구매내역 3개리스트

        // 판매내역 갯수
        countSalesBid(model, memberDTO);

        List<MyProductDTO> salesList = memberService.salesTop3List(memberDTO.getMno());
        model.addAttribute("salesList", salesList); // 판매입찰 내역 3개 리스트

        return "myPage/home";
    }

    @GetMapping("/buying")
    public void buying(Model model, @AuthenticationPrincipal MemberDTO memberDTO, MySearchDTO mySearchDTO, Pageable pageable){
        // 구매내역
        countPurchaseBid(model, memberDTO);

        Page<MyProductDTO> purchaseList = memberService.allPurchase_lt6(mySearchDTO, pageable, memberDTO.getMno());
        model.addAttribute("purchaseList", purchaseList); // 6개월 이내 목록

        int pageNum = purchaseList.hasNext() ? pageable.getPageNumber() : 1; // 목록이 1페이지뿐이면 페이지번호를 1로 재설정

        model.addAttribute("searchToDate", mySearchDTO.getSearchToDate());
        model.addAttribute("searchFromDate", mySearchDTO.getSearchFromDate());
        model.addAttribute("type", mySearchDTO.getType());
        model.addAttribute("page", pageNum);
    }


    @GetMapping("/selling")
    public void selling(Model model, @AuthenticationPrincipal MemberDTO memberDTO, MySearchDTO mySearchDTO, Pageable pageable){
        // 판매내역
        countSalesBid(model, memberDTO);

        Page<MyProductDTO> salesList = memberService.allSales_lt6(mySearchDTO, pageable, memberDTO.getMno());
        model.addAttribute("salesList", salesList);

        int pageNum = salesList.hasNext() ? pageable.getPageNumber() : 1; // 목록이 1페이지뿐이면 페이지번호를 1로 재설정

        model.addAttribute("searchToDate", mySearchDTO.getSearchToDate());
        model.addAttribute("searchFromDate", mySearchDTO.getSearchFromDate());
        model.addAttribute("type", mySearchDTO.getType());
        model.addAttribute("page", pageNum);
    }

    private void countSalesBid(Model model, @AuthenticationPrincipal MemberDTO memberDTO) {
        int allSalesBid = memberService.countAllSalesBidByMno(memberDTO.getMno());
        model.addAttribute("allSalesBid", allSalesBid); // 판매입찰 내역 전체갯수

        int falseSalesBid = memberService.countSalesBidIsBuyFalse(memberDTO.getMno());
        model.addAttribute("falseSalesBid", falseSalesBid); // 판매입찰 내역 진행중 갯수

        int trueSalesBid = memberService.countSalesBidIsBuyTrue(memberDTO.getMno());
        model.addAttribute("trueSalesBid", trueSalesBid); // 판매입찰 내역 종료 갯수
    }

    private void countPurchaseBid(Model model, @AuthenticationPrincipal MemberDTO memberDTO) {
        int allPurchaseBid = memberService.countAllPurchaseBidByMno(memberDTO.getMno());
        model.addAttribute("allPurchaseBid", allPurchaseBid); // 구매내역 전체갯수

        int falsePurchaseBid = memberService.countPurchaseBidIsBuyFalse(memberDTO.getMno());
        model.addAttribute("falsePurchaseBid", falsePurchaseBid); // 구매내역 진행중갯수

        int truePurchaseBid = memberService.countPurchaseBidIsBuyTrue(memberDTO.getMno());
        model.addAttribute("truePurchaseBid", truePurchaseBid); // 구매내역 종료갯수
    }
}
