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
    private Long pno; // 고유번호 PK

    @Column(nullable = false)
    private String productName; // 상품이름

    @Column(nullable = false)
    private String brand; // 상품 브랜드

    @Column(nullable = false)
    private String size; // 사이즈

    @Column(nullable = false)
    private int price; // 가격


}
