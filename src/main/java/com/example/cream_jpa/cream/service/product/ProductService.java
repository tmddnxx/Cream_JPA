package com.example.cream_jpa.cream.service.product;

import com.example.cream_jpa.cream.dto.ProductDTO;
import com.example.cream_jpa.cream.dto.Purchase_bidDTO;
import com.example.cream_jpa.cream.dto.Sales_bidDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    void register(ProductDTO productDTO); // 상품등록
    Page<ProductDTO> getAllProduct(String keyword, Pageable pageable); // 상품목록 출력 ( 페이징 )
    Optional<ProductDTO> getOne(Long pno); // 상품 상세

    void modifyOne(ProductDTO productDTO); // 상품 수정
    void removeOne(Long pno); // 삭제

    List<ProductDTO> getQuote(Long pno); // 시세 가져오기

    void deal(); // 자동 체결처리
    
//    void deleteOldRecord(); // 30일 지난 입찰되지않은 레코드 삭제
    
    List<Purchase_bidDTO> recentPurchasePrices(Long pno); // 최근 구매거래가
    List<Sales_bidDTO> recentSalesPrices(Long pno); // 최근 구매거래가

    void updateProductImg(String fileName, Long pno); // 수정view에서 기존 이미지 삭제시 DB에서 지움
}
