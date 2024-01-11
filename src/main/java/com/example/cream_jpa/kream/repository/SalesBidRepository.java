package com.example.cream_jpa.kream.repository;

import com.example.cream_jpa.kream.entity.Sales_bid;
import com.example.cream_jpa.kream.repository.queryDSL.QueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesBidRepository extends JpaRepository<Sales_bid, Long>{

    Long countSales_bidByMnoAndProductPno(Long mno, Long pno); // 내가 특정삼품에 판매입찰을 넣었는지

    Optional<Sales_bid> findSales_bidByMnoAndProductPno(Long mno, Long pno); // 판매입찰 넣은 엔티티 가져오기
}
