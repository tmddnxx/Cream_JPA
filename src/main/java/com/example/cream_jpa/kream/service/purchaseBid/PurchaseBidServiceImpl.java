package com.example.cream_jpa.kream.service.purchaseBid;

import com.example.cream_jpa.kream.dto.Purchase_bidDTO;
import com.example.cream_jpa.kream.entity.Purchase_bid;
import com.example.cream_jpa.kream.entity.Sales_bid;
import com.example.cream_jpa.kream.repository.PurchaseBidRepository;
import com.example.cream_jpa.kream.repository.SalesBidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PurchaseBidServiceImpl implements PurchaseBidService{

    private final PurchaseBidRepository purchaseBidRepository;
    private final SalesBidRepository salesBidRepository;

    @Override // 구매 입찰
    public void addPurchase(Purchase_bidDTO purchase_bidDTO) {
        Purchase_bid purchase_bid = purchase_bidDTO.toEntity(); // 엔티티로 변환

        purchaseBidRepository.save(purchase_bid);

    }

    @Override // 즉시 구매
    public void purChaseNow(Purchase_bidDTO purchase_bidDTO) {
        // 판매입찰목록에서 해당 엔티티 찾아서 판매처리시켜버림
        Purchase_bid purchase_bid = purchase_bidDTO.toEntity();

        Optional<Sales_bid> optionalSales_bid = salesBidRepository.findTopByProductPnoAndSalesPriceAndIsBuyOrderByBidDateAsc(purchase_bid.getProduct().getPno(), purchase_bid.getPurchasePrice(), false);

        if (optionalSales_bid.isPresent()) {
            log.info("존재함");

            Sales_bid sales_bid = optionalSales_bid.get();
            sales_bid.bought(purchase_bid.getMno());
            salesBidRepository.save(sales_bid);
        } else {
            log.info("존재하지 않음");
            // 존재하지 않을 때의 처리
        }
    }


}
