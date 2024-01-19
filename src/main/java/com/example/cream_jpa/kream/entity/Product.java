package com.example.cream_jpa.kream.entity;

import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.kream.repository.ProductRepository;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno; // 고유번호 PK , 상품코드


    @Column(nullable = false)
    private String productName; // 상품이름

    @Column(nullable = false)
    private String productImg; // 상품이미지

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<Sales_bid> sales_bids = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<Purchase_bid> purchase_bids = new ArrayList<>();

    public ProductDTO toDTO() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName(this.productName);
        productDTO.setPno(this.pno);
        return productDTO;
    }

    public void ChangeName(String productName){
        this.productName = productName;
    }
}
