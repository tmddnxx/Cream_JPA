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
public class Sales_bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sbNo; // PK값

    @Column(nullable = false)
    private String pno; // 상품코드

    @Column(nullable = false)
    private Long mno; // 판매자 고유번호

    @Column(nullable = false)
    private int salesPrice; // 판매입찰가
}