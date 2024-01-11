package com.example.cream_jpa.kream.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase_bid{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pbNo;

    @Column(nullable = false)
    private Long mno; // 구매자 고유번호

    @Column(nullable = false)
    private int purchasePrice; // 구매입찰가

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime bidDate; // 입찰날짜

    @ManyToOne
    @JoinColumn(name = "pno", referencedColumnName = "pno")
    private Product product; // 상품코드에 대한 연관 관계


    public void changePrice(int purchasePrice){ // 구매입찰을 했다면 가격바꿈
        this.purchasePrice = purchasePrice;

    }
}
