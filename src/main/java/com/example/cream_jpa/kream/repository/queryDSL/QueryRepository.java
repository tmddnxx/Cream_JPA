package com.example.cream_jpa.kream.repository.queryDSL;

import com.example.cream_jpa.kream.entity.Product;
import groovy.lang.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QueryRepository{

    int getMinSalesPrice(Long pno, boolean isBuy); // 최저판매가
    
    int getMaxPurchasePrice(Long pno, boolean isBuy); // 최고구매가

    int getQuote(Long pno, LocalDate buyDate); // 구매/판매 시세

    Page<Product> PRODUCT_PAGE(String keyword, Pageable pageable);
}
