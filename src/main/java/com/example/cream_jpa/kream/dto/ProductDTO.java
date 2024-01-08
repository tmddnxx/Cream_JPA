package com.example.cream_jpa.kream.dto;

import com.example.cream_jpa.kream.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long pno; // 고유번호 PK, 상품코드


    private Long mno; // 초기판매자의 고유번호

    private String productName; // 상품이름

    private int price; // 판매자 초기설정 가격

    public Product toEntity(){
        Product product = new Product();
        product.setMno(this.mno);
        product.setProductName(this.productName);
        product.setPrice(this.price);

        return product;
    }

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getPno(), product.getMno(), product.getProductName(), product.getPrice());
    }
}
