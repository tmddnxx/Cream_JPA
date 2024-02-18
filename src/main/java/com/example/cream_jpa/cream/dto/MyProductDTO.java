package com.example.cream_jpa.cream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyProductDTO {

    private Long pno; // 고유번호 PK, 상품코드


    private Long mno; // 내 고유번호

    private String productName; // 상품이름

    private List<String> productImg = new ArrayList<>(); // 상품 이미지 List
    
    private LocalDate bidDate; // 입찰날짜
}
