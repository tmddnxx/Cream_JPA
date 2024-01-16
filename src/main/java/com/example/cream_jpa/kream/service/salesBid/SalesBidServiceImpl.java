package com.example.cream_jpa.kream.service.salesBid;

import com.example.cream_jpa.kream.dto.Purchase_bidDTO;
import com.example.cream_jpa.kream.dto.Sales_bidDTO;
import com.example.cream_jpa.kream.entity.Purchase_bid;
import com.example.cream_jpa.kream.entity.Sales_bid;
import com.example.cream_jpa.kream.repository.PurchaseBidRepository;
import com.example.cream_jpa.kream.repository.SalesBidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class SalesBidServiceImpl implements SalesBidService{

    private final SalesBidRepository salesBidRepository;
    private final PurchaseBidRepository purchaseBidRepository;

    @Override // 판매입찰 등록
    public void addSales(Sales_bidDTO sales_bidDTO) {
        Sales_bid sales_bid = sales_bidDTO.toEntity();


        salesBidRepository.save(sales_bid);
    }

    @Override // 즉시판매
    public void saleNow(Sales_bidDTO sales_bidDTO) {
        // 구매입찰목록에서 해당 엔티티 찾아서 구매처리시켜버림
        Sales_bid sales_bid = sales_bidDTO.toEntity();

        Optional<Purchase_bid> optionalPurchase_bid = purchaseBidRepository.findTopByProductPnoAndPurchasePriceAndIsBuyOrderByBidDateAsc(sales_bid.getProduct().getPno(), sales_bid.getSalesPrice(), false);

        if(optionalPurchase_bid.isPresent()){
            optionalPurchase_bid.get().bought(sales_bid.getMno());
            purchaseBidRepository.save(optionalPurchase_bid.get());
        }

    }
}
