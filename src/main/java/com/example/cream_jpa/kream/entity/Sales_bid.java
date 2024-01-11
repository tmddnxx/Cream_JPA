package com.example.cream_jpa.kream.entity;

import com.example.cream_jpa.kream.dto.Sales_bidDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
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

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime bidDate; // 입찰날짜

    @ManyToOne
    @JoinColumn(name = "pno", referencedColumnName = "pno")
    private Product product; // 상품코드에 대한 연관 관계


    public void changePrice(int salesPrice){ // 판매입찰을 했다면 가격바꿈
        this.salesPrice = salesPrice;

    }
}
