package com.example.cream_jpa.kream.dto;

import com.example.cream_jpa.kream.entity.Product;
import com.example.cream_jpa.kream.entity.Sales_bid;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales_bidDTO {

    private Long sbNo; // PK값
    private Long pno; // 상품코드
    private Long mno; // 판매자 고유번호
    private int salesPrice; // 판매입찰가
    
    private LocalDateTime bidDate; // 입찰날짜

    public Sales_bid toEntity(){
        Product product = Product.builder()
                .pno(this.pno)
                .build();

        Sales_bid sales_bid = Sales_bid.builder()
                .mno(this.mno)
                .salesPrice(this.salesPrice)
                .bidDate(LocalDateTime.now())
                .product(product)
                .build();
        return sales_bid;
    }
}
