package com.example.cream_jpa.kream.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales_bidDTO {

    private Long sbNo; // PK값
    private String pno; // 상품코드
    private Long mno; // 판매자 고유번호
    private int salesPrice; // 판매입찰가
}
