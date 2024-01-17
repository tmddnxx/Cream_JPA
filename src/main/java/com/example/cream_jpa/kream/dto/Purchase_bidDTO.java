package com.example.cream_jpa.kream.dto;

import com.example.cream_jpa.kream.entity.Product;
import com.example.cream_jpa.kream.entity.Purchase_bid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase_bidDTO {

    private Long pbNo; // index값
    private Long pno; // 상품코드
    private Long mno; // 구매자 고유번호
    private int purchasePrice; // 구매입찰가

    private LocalDateTime bidDate; // 입찰날짜

    private boolean isBuy; // 판매여부

    private Long buyMno; // 구매자

    private LocalDate buyDate; // 체결날짜


    public Purchase_bid toEntity(){
        Product product = Product.builder()
                .pno(this.pno)
                .build();

        Purchase_bid purchase_bid = Purchase_bid.builder()
                .mno(this.mno)
                .purchasePrice(this.purchasePrice)
                .bidDate(LocalDateTime.now())
                .isBuy(this.isBuy)
                .buyMno(this.buyMno)
                .buyDate(this.buyDate)
                .product(product)
                .build();

        return purchase_bid;
    }
}
