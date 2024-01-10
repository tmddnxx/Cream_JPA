package com.example.cream_jpa.kream.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Sales_bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sbNo;

    @Column(nullable = false)
    private Long mno; // 판매자 고유번호

    @Column(nullable = false)
    private int salesPrice; // 판매입찰가

    @ManyToOne
    @JoinColumn(name = "pno", referencedColumnName = "pno")
    private Product product; // 상품코드에 대한 연관 관계


}
