package com.example.cream_jpa.kream.service.salesBid;

import com.example.cream_jpa.kream.dto.Purchase_bidDTO;
import com.example.cream_jpa.kream.dto.Sales_bidDTO;

public interface SalesBidService {
    
    void addSales(Sales_bidDTO sales_bidDTO); // 판매입찰 등록

    void saleNow(Sales_bidDTO sales_bidDTO); // 즉시판매
}
