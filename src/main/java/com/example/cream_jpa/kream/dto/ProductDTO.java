package com.example.cream_jpa.kream.dto;

import com.example.cream_jpa.kream.entity.Product;
import com.example.cream_jpa.kream.entity.Sales_bid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long pno; // 고유번호 PK, 상품코드


    private Long mno; // 초기판매자의 고유번호

    private String productName; // 상품이름

    private int price; // 판매자 초기설정 가격

    public Product toEntity(){
        Product product = Product.builder()
                .pno(this.pno)
                .productName(this.productName)
                .build();


        return product;
    }


}
