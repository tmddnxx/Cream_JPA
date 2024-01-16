package com.example.cream_jpa.kream.service.product;

import com.example.cream_jpa.kream.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    void register(ProductDTO productDTO); // 상품등록
    Page<ProductDTO> getAllProduct(Pageable pageable); // 상품목록 출력 ( 페이징 )
    Optional<ProductDTO> getOne(Long pno); // 상품 상세

    void modifyOne(ProductDTO productDTO); // 상품 수정
    void removeOne(Long pno); // 삭제

    List<ProductDTO> getQuote(Long pno); // 시세 가져오기

    void deal(); // 자동 체결처리
    
    List<ProductDTO> recentPrices(Long pno); // 최근 거래가

}
