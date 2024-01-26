package com.example.cream_jpa.repository;

import com.example.cream_jpa.kream.dto.MyProductDTO;
import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.kream.entity.Product;
import com.example.cream_jpa.kream.entity.Purchase_bid;
import com.example.cream_jpa.kream.repository.ProductRepository;
import com.example.cream_jpa.kream.repository.PurchaseBidRepository;
import com.example.cream_jpa.kream.repository.queryDSL.QueryRepository;
import com.example.cream_jpa.member.repository.MemberRepository;
import com.querydsl.core.Tuple;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class test {

    @Autowired
    private PurchaseBidRepository purchaseBidRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

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
}
