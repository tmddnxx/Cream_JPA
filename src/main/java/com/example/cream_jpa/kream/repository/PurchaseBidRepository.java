package com.example.cream_jpa.kream.repository;

import com.example.cream_jpa.kream.entity.Purchase_bid;
import com.example.cream_jpa.kream.entity.Sales_bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.OptionalInt;

public interface PurchaseBidRepository extends JpaRepository<Purchase_bid, Long> {

    Long countPurchase_bidByMnoAndProductPno(Long mno, Long pno); //내가 특정상품에 구매입찰을 넣었는지

    Optional<Purchase_bid> findPurchase_bidByMnoAndProductPno(Long mno, Long pno); //구매입찰 넣은 엔티티 가져오기
}
