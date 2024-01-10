package com.example.cream_jpa.kream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase_bidDTO {

    private Long pbNo; // index값
    private Long pno; // 상품코드
    private Long mno; // 구매자 고유번호
    private int purchasePrice; // 구매입찰가
}
