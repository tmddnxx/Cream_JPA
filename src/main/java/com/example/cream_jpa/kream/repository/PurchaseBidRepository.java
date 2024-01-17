package com.example.cream_jpa.kream.repository;

import com.example.cream_jpa.kream.entity.Purchase_bid;
import com.example.cream_jpa.kream.entity.Sales_bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface PurchaseBidRepository extends JpaRepository<Purchase_bid, Long> {

    // 아직 구매하지않은 특정 상품에 특정 가격들 중 가장 오래된 엔티티 가져오기.
    Optional<Purchase_bid> findTopByProductPnoAndPurchasePriceAndIsBuyOrderByBidDateAsc(Long pno, int purchasePrice, boolean isBuy);

    List<Purchase_bid> findByProductPnoAndIsBuyFalseOrderByBidDateAsc(Long pno); // 특정상품에 체결되지않은 레코드 오래된 순으로 정렬한 List

    List<Purchase_bid> findTop5ByProductPnoAndIsBuyTrueOrderByBuyDateDesc(Long pno); // 특정 상품의 최근거래 5개 List

    List<Purchase_bid> findByIsBuyFalseAndBidDateBefore(LocalDateTime thirtyDaysAgo); // 30일이 경과한 입찰되지 않은 레코드 목록 -> 삭제

}
