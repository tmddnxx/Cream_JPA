package com.example.cream_jpa.kream.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno; // 고유번호 PK , 상품코드

    @Column(nullable = false)
    private Long mno; // 초기판매자의 고유번호

    @Column(nullable = false)
    private String productName; // 상품이름

    @Column(nullable = false)
    private int price; // 판매자 초기설정 가격
}
