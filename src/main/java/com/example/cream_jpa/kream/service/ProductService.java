package com.example.cream_jpa.kream.service;

import com.example.cream_jpa.kream.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    void register(ProductDTO productDTO); // 상품등록
    Page<ProductDTO> getAllProduct(Pageable pageable); // 상품목록 출력 ( 페이징 )
    Optional<ProductDTO> getOne(Long pno); // 상품 상세

    void modifyOne(ProductDTO productDTO); // 상품 수정
    void removeOne(Long pno); // 삭제
}
