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
public class Purchase_bid{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pbNo;

    @Column(nullable = false)
    private Long mno; // 구매자 고유번호

    @Column(nullable = false)
    private int purchasePrice; // 구매입찰가

    @ManyToOne
    @JoinColumn(name = "pno", referencedColumnName = "pno")
    private Product product; // 상품코드에 대한 연관 관계

}
