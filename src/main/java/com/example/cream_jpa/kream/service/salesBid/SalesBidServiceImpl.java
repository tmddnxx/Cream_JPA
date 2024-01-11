package com.example.cream_jpa.kream.service.salesBid;

import com.example.cream_jpa.kream.dto.Sales_bidDTO;
import com.example.cream_jpa.kream.entity.Sales_bid;
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

    @Override // 판매입찰 등록
    public void addSales(Sales_bidDTO sales_bidDTO) {
        Sales_bid sales_bid = sales_bidDTO.toEntity();

        Long isBid = salesBidRepository.countSales_bidByMnoAndProductPno(sales_bidDTO.getMno(), sales_bid.getProduct().getPno());
        Optional<Sales_bid> updateSales = salesBidRepository.findSales_bidByMnoAndProductPno(sales_bidDTO.getMno(), sales_bid.getProduct().getPno());


        if(isBid == 0){ // 판매입찰한적이 없으면 새로 생성
            salesBidRepository.save(sales_bid);
        }else if (isBid == 1){ // 있으면 가격만 업데이트
            Sales_bid actualSales = updateSales.get();
            actualSales.changePrice(sales_bid.getSalesPrice());
            salesBidRepository.save(actualSales);
        }

    }
}
