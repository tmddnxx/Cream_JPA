package com.example.cream_jpa.cream.repository.queryDSL;

import com.example.cream_jpa.cream.dto.MyProductDTO;
import com.example.cream_jpa.cream.entity.Product;
import com.example.cream_jpa.myPage.dto.MySearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QueryRepository{

    int getMinSalesPrice(Long pno, boolean isBuy); // 최저판매가
    
    int getMaxPurchasePrice(Long pno, boolean isBuy); // 최고구매가

    int getQuote(Long pno, LocalDate buyDate); // 구매/판매 시세

    Page<Product> PRODUCT_PAGE(String keyword, Pageable pageable); // 검색, 페이징
    
    void removeProductImg(String fileName, Long pno); // 수정페이지에서 기존 이미지 삭제시 DB에서 지워짐

    List<MyProductDTO> purchaseListTop3(Long mno); // 특정회원 구매입찰목록 3개
    
    List<MyProductDTO> salesListTop3(Long mno); // 특정회원 판매입찰목록 3개

    Page<MyProductDTO> allPurchaseList_lt6(MySearchDTO mySearchDTO, Pageable pageable, Long mno); // 특정회원의 구매내역목록전체(6개월미만)
    Page<MyProductDTO> allSalesList_lt6(MySearchDTO mySearchDTO, Pageable pageable, Long mno); // 특정회원의 판매내역목록전체(6개월미만)
}
