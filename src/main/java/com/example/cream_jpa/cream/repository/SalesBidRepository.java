package com.example.cream_jpa.cream.repository;

import com.example.cream_jpa.cream.entity.Sales_bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesBidRepository extends JpaRepository<Sales_bid, Long>{

    // 아직 구매하지않은 특정 상품에 특정 가격들 중 가장 오래된 엔티티 가져오기.
    Optional<Sales_bid> findTopByProductPnoAndSalesPriceAndIsBuyOrderByBidDateAsc(Long pno, int salesPrice, boolean isBuy);

    List<Sales_bid> findByProductPnoAndIsBuyFalseOrderByBidDateAsc(Long pno); // 특정상품에 체결되지않은 레코드 오래된 순으로 정렬한 List

    List<Sales_bid> findTop5ByProductPnoAndIsBuyTrueOrderByBuyDateDesc(Long pno); // 특정 상품의 최근거래 5개 List

    List<Sales_bid> findByIsBuyFalseAndBidDateBefore(LocalDateTime thirtyDaysAgo); // 30일이 경과한 입찰되지 않은 레코드 목록 -> 삭제

    int countSales_bidByMno(Long mno); // 특정회원의 판매입찰 전체 찾기
    int countSales_bidByMnoAndIsBuyFalse(Long mno); // 특정회원 판매입찰 진행중 찾기
    int countSales_bidByMnoAndIsBuyTrue(Long mno); // 특정회원 판매입찰 완료 찾기
}
