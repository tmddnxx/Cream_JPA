package com.example.cream_jpa.kream.service.purchaseBid;

import com.example.cream_jpa.kream.dto.Purchase_bidDTO;
import com.example.cream_jpa.kream.entity.Purchase_bid;
import com.example.cream_jpa.kream.repository.PurchaseBidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseBidServiceImpl implements PurchaseBidService{

    private final PurchaseBidRepository purchaseBidRepository;

    @Override // 구매 입찰
    public void addPurchase(Purchase_bidDTO purchase_bidDTO) {
        Purchase_bid purchase_bid = purchase_bidDTO.toEntity(); // 엔티티로 변환

        Optional<Purchase_bid> optionalPurchase_bid = purchaseBidRepository.findPurchase_bidByMnoAndProductPno(purchase_bid.getMno(), purchase_bidDTO.getPno());
        Long isBid = purchaseBidRepository.countPurchase_bidByMnoAndProductPno(purchase_bid.getMno(), purchase_bidDTO.getPno());

        if(isBid == 0){ // 구매입찰이 없으면 새로 저장
            purchaseBidRepository.save(purchase_bid);
        }else if(isBid == 1){ // 있으면 가격만 바꿔서 저장.
            Purchase_bid purchaseBid = optionalPurchase_bid.get();
            purchaseBid.changePrice(purchase_bidDTO.getPurchasePrice());
            purchaseBidRepository.save(purchaseBid);
        }
    }
}
