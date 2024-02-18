package com.example.cream_jpa.repository;

import com.example.cream_jpa.cream.dto.MyProductDTO;
import com.example.cream_jpa.cream.entity.Purchase_bid;
import com.example.cream_jpa.cream.repository.ProductRepository;
import com.example.cream_jpa.cream.repository.PurchaseBidRepository;
import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.repository.MemberRepository;
import com.example.cream_jpa.member.service.MemberService;
import com.example.cream_jpa.security.CustomUserDetailService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
@Log4j2
public class test {

    @Autowired
    private PurchaseBidRepository purchaseBidRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Test
    public void addTest(){

        Optional<Purchase_bid> optionalPurchase_bid =
                purchaseBidRepository.findTopByProductPnoAndPurchasePriceAndIsBuyOrderByBidDateAsc(2L,5000, false);

        optionalPurchase_bid.get().bought(3L);

        System.out.println("테스트"+optionalPurchase_bid.get());
    }

    @Test
    public void recent(){
        List<Purchase_bid> purchase_bids = purchaseBidRepository.findTop5ByProductPnoAndIsBuyTrueOrderByBuyDateDesc(2L);

        for (Purchase_bid purchase_bid : purchase_bids){
            System.out.println("최근5"+purchase_bid.getPurchasePrice());
        }
    }

    @Test
    public void thirtyDaysAgo(){
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Purchase_bid> purchase_bids = purchaseBidRepository.findByIsBuyFalseAndBidDateBefore(thirtyDaysAgo);

        for (Purchase_bid purchase_bid : purchase_bids){
            System.out.println("비드"+purchase_bid.getBidDate());
        }
    }

    @Test
    @Transactional
    public void purchaseListTop3(){

        List<MyProductDTO> productList = productRepository.purchaseListTop3(3L);

        for (MyProductDTO product : productList){
            System.out.println("구매내역 ? " + product);
        }
    }

    @Test
    public void findpw(){
        MemberDTO memberDTO = memberService.findByMno(12L);
        String dbPasswd = memberDTO.getPasswd();
        // 사용자가 입력한 평문비밀번호와 암호화된 비밀번호 일치확인
        boolean pwCheck = customUserDetailService.passwordEncoder().matches("2O46LCBB", dbPasswd);
        log.info(pwCheck);
    }

    @Test
    public void random(){
        Random random = new Random();
        StringBuilder randomStr = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            if (random.nextBoolean()) {
                // 랜덤 대문자 추가
                char randomChar = (char) ('A' + random.nextInt(26));
                randomStr.append(randomChar);
            } else {
                // 랜덤 숫자 추가
                randomStr.append(random.nextInt(10));
            }
        }

        char[] specialChars = {'!', '@', '#', '$', '%'};
        randomStr.append(specialChars[random.nextInt(specialChars.length)]);

        log.info("랜덤 " + randomStr);
    }
}
