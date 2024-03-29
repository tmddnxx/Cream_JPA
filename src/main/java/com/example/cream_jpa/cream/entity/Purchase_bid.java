package com.example.cream_jpa.cream.entity;

import com.example.cream_jpa.cream.dto.Purchase_bidDTO;
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

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isBuy; // 구매완료

    @Column
    private Long buyMno; // 판매자;
    
    @Column
    private LocalDate buyDate; // 체결날짜

    @ManyToOne
    @JoinColumn(name = "pno", referencedColumnName = "pno")
    private Product product; // 상품코드에 대한 연관 관계


    public Purchase_bidDTO toDTO(){
        Purchase_bidDTO purchase_bidDTO = new Purchase_bidDTO();
        purchase_bidDTO.setPbNo(this.pbNo);
        purchase_bidDTO.setMno(this.mno);
        purchase_bidDTO.setPurchasePrice(this.purchasePrice);
        purchase_bidDTO.setBidDate(this.bidDate);
        purchase_bidDTO.setBuyMno(this.buyMno);
        purchase_bidDTO.setBuyDate(this.buyDate);
        purchase_bidDTO.setPno(this.product.getPno());

        return purchase_bidDTO;
    }

    public void bought(Long buyMno){ // 구매처리
        this.isBuy = true;
        this.buyMno = buyMno;
        this.buyDate = LocalDate.now();
    }

}
