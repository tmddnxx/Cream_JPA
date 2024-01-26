package com.example.cream_jpa.kream.dto;

import com.example.cream_jpa.kream.entity.Product;
import com.example.cream_jpa.kream.entity.Sales_bid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long pno; // 고유번호 PK, 상품코드


    private Long mno; // 초기판매자의 고유번호

    private String productName; // 상품이름

    @Builder.Default
    private List<String> productImg = new ArrayList<>(); // 상품 이미지 List

    private int salesPrice; // 판매자 초기설정 가격

    private int purchasePrice; // 구매 입찰가 중 비싼 가격.

    private int quote; // 시세
    private LocalDate buyDate; // 체결날짜

    public Product toEntity(){
        Product product = Product.builder()
                .pno(this.pno)
                .productName(this.productName)
                .build();
        return product;
    }


}
