package com.example.cream_jpa.cream.service.purchaseBid;

import com.example.cream_jpa.cream.dto.Purchase_bidDTO;

public interface PurchaseBidService {
    
    void addPurchase(Purchase_bidDTO purchase_bidDTO); //구매입찰

    void purChaseNow(Purchase_bidDTO purchase_bidDTO); // 즉시구매
}
