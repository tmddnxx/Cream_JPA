package com.example.cream_jpa.kream.service.product;

import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.kream.dto.Purchase_bidDTO;
import com.example.cream_jpa.kream.dto.Sales_bidDTO;
import com.example.cream_jpa.kream.entity.Product;
import com.example.cream_jpa.kream.entity.Purchase_bid;
import com.example.cream_jpa.kream.entity.Sales_bid;
import com.example.cream_jpa.kream.repository.ProductRepository;
import com.example.cream_jpa.kream.repository.PurchaseBidRepository;
import com.example.cream_jpa.kream.repository.SalesBidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final SalesBidRepository salesBidRepository;
    private final PurchaseBidRepository purchaseBidRepository;


    @Override
    @Transactional
    public void register(ProductDTO productDTO) { // 상품등록

        String productImg;
        List<String> files = productDTO.getProductImg();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i < files.size(); i++){
            String file = files.get(i);
            stringBuilder.append(file);

            if(i < files.size()-1){
                stringBuilder.append(", ");
            }
        }

        productImg = stringBuilder.toString();

        // ProductDTO를 이용하여 Product 엔티티 생성
        Product product = Product.builder()
                .productImg(productImg)
                .productName(productDTO.getProductName())
                .build();

        // Product 엔티티 저장
        productRepository.save(product);

        // Sales_bid 엔티티 생성 및 설정
        Sales_bid salesBid = new Sales_bid(null,productDTO.getMno(),productDTO.getSalesPrice(), LocalDateTime.now(), false, null, null, product);

        // Sales_bid 엔티티 저장
        salesBidRepository.save(salesBid);
    }

    @Override
    public Page<ProductDTO> getAllProduct(String keyword, Pageable pageable) {
        // 정렬 기준을 'pno' 기준으로 내림차순으로 설정합니다.
        Sort sort = Sort.by("pno").descending();

        // 페이지 요청 정보(pageable)에 정렬을 적용하여 새로운 페이지 요청 정보를 생성합니다.
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Product> productPage;
        // 실제로 데이터베이스에서 페이징 및 정렬된 데이터를 조회합니다.
        if(keyword != null){
            productPage = productRepository.PRODUCT_PAGE(keyword, pageable);
            log.info(" 검색 토탈 페이지 "+productPage.getTotalElements());
        }else {
            productPage = productRepository.findAll(pageable);
            log.info("전체 갯수"+ productPage.getTotalElements());
        }

        // 조회된 엔터티 목록을 ProductDTO로 변환합니다.
        // getContent - Page에 포함된 엔티티목록가져오는메서드
        // stream - 순차 및 병렬처리
        // map - Product의 toDTO메서드를 참조해서 각각의 엔티티를 DTO로 변환함
        // collect - 스트림의 각 요소를 리스트로 수집
        //Collectors.toList - 리스트로 변환
        List<ProductDTO> productDTOList = productPage.getContent().stream()
                .map(product -> {
                    ProductDTO productDTO = product.toDTO();

                    String[] productImg = product.getProductImg().split(", "); // String 이미지 자르기
                    List<String> productImgList = Arrays.asList(productImg); // 배열로 만듬

                    productDTO.setProductImg(productImgList);
                    productDTO.setPurchasePrice(productRepository.getMaxPurchasePrice(product.getPno(), false));
                    productDTO.setSalesPrice(productRepository.getMinSalesPrice(product.getPno(), false));
                    return productDTO;
                })
                .collect(Collectors.toList());

        // ProductDTO 목록과 페이지 정보를 사용하여 새로운 Page<ProductDTO>를 생성합니다.
        // PageImpl은 Spring Data에서 제공하는 Page 구현체 중 하나입니다.
        // 생성자의 인자로는 변환된 목록, 페이지 요청 정보, 총 엔터티 개수가 필요합니다.
        return new PageImpl<>(productDTOList, pageable, productPage.getTotalElements());
    }

    @Override
    public Optional<ProductDTO> getOne(Long pno) { // 상품상세
        Optional<Product> product = productRepository.findById(pno);

        ProductDTO productDTO = new ProductDTO();

        String[] productImg = product.get().getProductImg().split(", ");
        List<String> productImgList = Arrays.asList(productImg);

        productDTO.setPno(product.get().getPno());
        productDTO.setProductImg(productImgList);
        productDTO.setProductName(product.get().getProductName());
        productDTO.setPurchasePrice(productRepository.getMaxPurchasePrice(product.get().getPno(), false));
        productDTO.setSalesPrice(productRepository.getMinSalesPrice(product.get().getPno(), false));

        return Optional.of(productDTO);
    }

    @Override
    public void modifyOne(ProductDTO productDTO) {
        // 주어진 ID로 엔터티 조회
        Optional<Product> optionalProduct = productRepository.findById(productDTO.getPno());
        Product product = optionalProduct.get();

        String productImg;
        List<String> files = productDTO.getProductImg();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i < files.size(); i++){
            String file = files.get(i);
            stringBuilder.append(file);

            if(i < files.size()-1){
                stringBuilder.append(", ");
            }
        }

        productImg = stringBuilder.toString();

        // ProductDTO를 이용하여 Product 엔티티 생성
        product = Product.builder()
                .productImg(productImg)
                .productName(productDTO.getProductName())
                .pno(productDTO.getPno())
                .build();

        productRepository.save(product);

    }

    @Override // 상품삭제
    public void removeOne(Long pno) {
        productRepository.deleteById(pno);
    }

    @Override // 시세 보기
    public List<ProductDTO> getQuote(Long pno) {
        List<ProductDTO> quoteList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for(int i=0; i<7; i++){
            LocalDate currentDay = today.minusDays(i);

            Integer quote = productRepository.getQuote(pno, currentDay);
            quoteList.add(ProductDTO.builder()
                    .pno(pno)
                    .quote(quote)
                    .buyDate(currentDay)
                    .build());
        }

        return quoteList;
    }


    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    @Override
    public void deal() { // 매일 자정에 각각 입찰가 비교 후 체결처리하기
        log.info("스케쥴");

        List<Product> products = productRepository.findAll();
        for (Product product : products){
            Long pno = product.getPno();

            List<Purchase_bid> purchase_bids = purchaseBidRepository.findByProductPnoAndIsBuyFalseOrderByBidDateAsc(pno);
            List<Sales_bid> sales_bids = salesBidRepository.findByProductPnoAndIsBuyFalseOrderByBidDateAsc(pno);

            for (Purchase_bid purchaseBid : purchase_bids) {
                // 각 Purchase_bid에 대해 가장 오래된 Sales_bid 찾기
                Optional<Sales_bid> optionalOldestSalesBid = salesBidRepository.findTopByProductPnoAndSalesPriceAndIsBuyOrderByBidDateAsc(
                        purchaseBid.getProduct().getPno(), purchaseBid.getPurchasePrice(), false);

                // 가장 오래된 Sales_bid에 대해만 처리
                optionalOldestSalesBid.ifPresent(oldestSalesBid -> {
                    purchaseBid.bought(oldestSalesBid.getMno());
                    oldestSalesBid.bought(purchaseBid.getMno());
                    purchaseBidRepository.save(purchaseBid);
                    salesBidRepository.save(oldestSalesBid);
                    log.info("12시가되면 처리해요");
                });
            }

            for (Sales_bid salesBid : sales_bids) {
                // 각 Sales_bid에 대해 가장 오래된 Purchase_bid 찾기
                Optional<Purchase_bid> optionalOldestPurchaseBid = purchaseBidRepository.findTopByProductPnoAndPurchasePriceAndIsBuyOrderByBidDateAsc(
                        salesBid.getProduct().getPno(), salesBid.getSalesPrice(), false);

                // 가장 오래된 Purchase_bid에 대해만 처리
                optionalOldestPurchaseBid.ifPresent(oldestPurchaseBid -> {
                    salesBid.bought(oldestPurchaseBid.getMno());
                    oldestPurchaseBid.bought(salesBid.getMno());
                    salesBidRepository.save(salesBid);
                    purchaseBidRepository.save(oldestPurchaseBid);
                    log.info("12시가되면 처리해요");
                });
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    @Override // 30일이 지난 입찰되지않은 레코드 삭제
    public void deleteOldRecord() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Purchase_bid> purchase_bids = purchaseBidRepository.findByIsBuyFalseAndBidDateBefore(thirtyDaysAgo);
        List<Sales_bid> sales_bids = salesBidRepository.findByIsBuyFalseAndBidDateBefore(thirtyDaysAgo);

        purchaseBidRepository.deleteAll(purchase_bids);
        salesBidRepository.deleteAll(sales_bids);
    }

    @Override // 최근 구매가 5개
    public List<Purchase_bidDTO> recentPurchasePrices(Long pno) {

        List<Purchase_bid> purchase_bids = purchaseBidRepository.findTop5ByProductPnoAndIsBuyTrueOrderByBuyDateDesc(pno);

        // 반환된 스트림을 새로운 리스트로 수집합니다.
        List<Purchase_bidDTO> purchase_bidDTOList = purchase_bids.stream()
                .map(Purchase_bid::toDTO)
                .collect(Collectors.toList());

        return purchase_bidDTOList;
    }

    @Override // 최근 판매가 5개
    public List<Sales_bidDTO> recentSalesPrices(Long pno) {

        List<Sales_bid> sales_bids = salesBidRepository.findTop5ByProductPnoAndIsBuyTrueOrderByBuyDateDesc(pno);

        List<Sales_bidDTO> sales_bidDTOList = sales_bids.stream()
                .map(Sales_bid::toDto)
                .collect(Collectors.toList());

        return sales_bidDTOList;
    }

    @Transactional
    @Override
    public void updateProductImg(String fileName, Long pno) {
        productRepository.removeProductImg(fileName, pno);
    }


}
