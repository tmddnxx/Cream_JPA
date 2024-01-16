package com.example.cream_jpa.kream.repository;

import com.example.cream_jpa.kream.entity.Purchase_bid;
import com.example.cream_jpa.kream.entity.Sales_bid;
import com.example.cream_jpa.kream.repository.queryDSL.QueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesBidRepository extends JpaRepository<Sales_bid, Long>{

    // 아직 구매하지않은 특정 상품에 특정 가격들 중 가장 오래된 엔티티 가져오기.
    Optional<Sales_bid> findTopByProductPnoAndSalesPriceAndIsBuyOrderByBidDateAsc(Long pno, int salesPrice, boolean isBuy);

    List<Sales_bid> findByProductPnoAndIsBuyFalseOrderByBidDateAsc(Long pno); // 특정상품에 체결되지않은 레코드 오래된 순으로 정렬한 List

    List<Sales_bid> findTop5ByProductPnoAndIsBuyTrueOrderByBuyDateAsc(Long pno); // 특정 상품의 최근거래 5개 List

}
