package com.example.cream_jpa.kream.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sales_bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sbNo;

    @Column(nullable = false)
    private Long mno; // 판매자 고유번호

    @Column(nullable = false)
    private int salesPrice; // 판매입찰가

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime bidDate; // 입찰날짜

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isBuy; // 판매완료

    @Column
    private Long buyMno; // 구매자

    @Column
    private LocalDate buyDate; // 체결 날짜

    @ManyToOne
    @JoinColumn(name = "pno", referencedColumnName = "pno")
    private Product product; // 상품코드에 대한 연관 관계



    public void bought(Long buyMno){ // 판매처리
        this.isBuy = true;
        this.buyMno = buyMno;
        this.buyDate = LocalDate.now();
    }
}
