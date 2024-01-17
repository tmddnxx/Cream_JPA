package com.example.cream_jpa.repository;

import com.example.cream_jpa.kream.entity.Purchase_bid;
import com.example.cream_jpa.kream.repository.PurchaseBidRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class test {

    @Autowired
    private PurchaseBidRepository purchaseBidRepository;

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
}
