package com.example.cream_jpa.kream.repository.queryDSL;
import com.example.cream_jpa.kream.dto.MyProductDTO;
import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.kream.entity.*;
import com.example.cream_jpa.myPage.dto.MySearchDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.querydsl.core.types.dsl.Expressions.*;
import static com.querydsl.core.types.dsl.Expressions.dateTemplate;
import static java.util.Optional.ofNullable;

@Log4j2
public class QueryRepositoryImpl extends QuerydslRepositorySupport implements QueryRepository {


    private final JPAQueryFactory jpaQueryFactory;

    public QueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(QProduct.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public int getMinSalesPrice(Long pno, boolean isBuy) {

        QProduct qProduct = QProduct.product;
        QSales_bid qSales_bid = QSales_bid.sales_bid;

        Integer minPrice =
                jpaQueryFactory.select(qSales_bid.salesPrice.min())
                        .from(qProduct)
                        .join(qProduct.sales_bids, qSales_bid)
                        .where(qProduct.pno.eq(pno))
                        .where(qSales_bid.isBuy.eq(false))
                        .fetchOne();
        if (minPrice != null){
            return minPrice;
        }else{
            return 0;
        }

    }

    @Override
    public int getMaxPurchasePrice(Long pno, boolean isBuy) {

        QProduct qProduct = QProduct.product;
        QPurchase_bid qPurchase_bid = QPurchase_bid.purchase_bid;

        Integer maxPrice =
                jpaQueryFactory.select(qPurchase_bid.purchasePrice.max())
                        .from(qProduct)
                        .join(qProduct.purchase_bids, qPurchase_bid)
                        .where(qProduct.pno.eq(pno))
                        .where(qPurchase_bid.isBuy.eq(false))
                        .fetchOne();
        if (maxPrice != null){
            return maxPrice;
        }else{
            return 0;
        }


    }

    @Override
    public int getQuote(Long pno, LocalDate buyDate) {
        QProduct qProduct = QProduct.product;
        QPurchase_bid qPurchase_bid = QPurchase_bid.purchase_bid;
        QSales_bid qSales_bid = QSales_bid.sales_bid;

        Integer purchaseQuote =
                ofNullable(jpaQueryFactory.select(qPurchase_bid.purchasePrice.avg().intValue())
                        .from(qProduct)
                        .join(qProduct.purchase_bids, qPurchase_bid)
                        .where(qProduct.pno.eq(pno))
                        .where(dateTemplate(LocalDate.class, "{0}", qPurchase_bid.buyDate).eq(buyDate))
                        .fetchOne())
                        .orElse(0);

        Integer salesQuote =
                ofNullable(jpaQueryFactory.select(qSales_bid.salesPrice.avg().intValue())
                        .from(qProduct)
                        .join(qProduct.sales_bids, qSales_bid)
                        .where(qProduct.pno.eq(pno))
                        .where(dateTemplate(LocalDate.class, "{0}", qSales_bid.buyDate).eq(buyDate))
                        .fetchOne())
                        .orElse(0);

        if(purchaseQuote == 0 && salesQuote != 0){
            return salesQuote;
        }else if(purchaseQuote != 0 && salesQuote == 0){
            return purchaseQuote;
        } else if(purchaseQuote != 0 && salesQuote != 0){
            return ((purchaseQuote + salesQuote) / 2);
        }

        return 0;
    }

    @Override // 검색기능
    public Page<Product> PRODUCT_PAGE(String keyword, Pageable pageable) {

        QProduct qProduct = QProduct.product;

        // 문자열로 변환해서 포함되는지 비교
        BooleanExpression predicate = qProduct.pno.stringValue().contains(keyword)
                .or(qProduct.productName.contains(keyword));

        // fetch() 메서드를 사용하여 JPAQueryFactory의 결과를 가져옵니다.
        List<Product> productList = jpaQueryFactory.select(qProduct) // qProduct 전체로 검색
                .from(qProduct)
                .where(predicate.
                        and(qProduct.isDel.eq(false)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //fetchCount가 없어질 예정이라 새롭게 만들어줌
        Long total = jpaQueryFactory.select(qProduct.count())
                .from(qProduct)
                .where(predicate)
                .fetchOne();

        // 가져온 결과를 Page 객체로 변환하여 반환합니다.
        return new PageImpl<>(productList, pageable, total);
    }

    @Transactional
    @Override
    public void removeProductImg(String fileName, Long pno) {
        QProduct qProduct = QProduct.product;
        CaseBuilder caseBuilder = new CaseBuilder();

        jpaQueryFactory.update(qProduct)
                .set(qProduct.productImg,
                        caseBuilder.when(qProduct.productImg.like("%"+fileName+", "+"%"))
                                .then(Expressions.stringTemplate("REPLACE({0}, {1}, '')", qProduct.productImg, fileName+", "))
                                .when(qProduct.productImg.like("%"+fileName+"%"))
                                .then(Expressions.stringTemplate("REPLACE({0}, {1}, '')", qProduct.productImg, fileName))
                                .otherwise(qProduct.productImg)
                                )
                .where(qProduct.pno.eq(pno))
                .execute();
    }

    @Transactional
    @Override
    public List<MyProductDTO> purchaseListTop3(Long mno) {
        QProduct qProduct = QProduct.product;
        QPurchase_bid qPurchase_bid = QPurchase_bid.purchase_bid;

        List<Tuple> tupleList = jpaQueryFactory.select(qProduct.pno, qProduct.productName, qProduct.productImg, qPurchase_bid.bidDate)
                .from(qProduct)
                .join(qProduct.purchase_bids, qPurchase_bid)
                .where(qPurchase_bid.mno.eq(mno))
                .orderBy(qPurchase_bid.bidDate.desc())
                .limit(3)
                .fetch();

        List<MyProductDTO> productDTOList = tupleList.stream().map(tuple -> {
            MyProductDTO productDTO = new MyProductDTO();

            // tuple에서 필요한 정보를 가져와서 productDTO에 매핑
            productDTO.setPno(tuple.get(qProduct.pno));
            productDTO.setProductName(tuple.get(qProduct.productName));
            productDTO.setMno(mno);

            // productImg를 쉼표로 구분하여 List로 변환
            String[] productImgArray = tuple.get(qProduct.productImg).split(", ");
            List<String> productImgList = Arrays.asList(productImgArray);
            productDTO.setProductImg(productImgList);

            // bidDate를 LocalDateTime으로 변환
            log.info("pbidDate " + tuple.get(qPurchase_bid.bidDate));
            LocalDate bidDate = tuple.get(qPurchase_bid.bidDate).toLocalDate();
            productDTO.setBidDate(bidDate);

            return productDTO;
        }).collect(Collectors.toList());

        return productDTOList;
    }

    @Override
    public List<MyProductDTO> salesListTop3(Long mno) {
        QProduct qProduct = QProduct.product;
        QSales_bid qSales_bid = QSales_bid.sales_bid;

        List<Tuple> tupleList = jpaQueryFactory.select(qProduct.pno, qProduct.productName, qProduct.productImg, qSales_bid.bidDate)
                .from(qProduct)
                .join(qProduct.sales_bids, qSales_bid)
                .where(qSales_bid.mno.eq(mno))
                .orderBy(qSales_bid.bidDate.desc())
                .limit(3)
                .fetch();

        log.info("tupleList ? " + tupleList);
        List<MyProductDTO> productDTOList = tupleList.stream().map(tuple -> {
            MyProductDTO productDTO = new MyProductDTO();

            productDTO.setPno(tuple.get(qProduct.pno));
            productDTO.setProductName(tuple.get(qProduct.productName));
            productDTO.setMno(mno);

            String[] imgList = tuple.get(qProduct.productImg).split(", ");

            List<String> stringList = Arrays.asList(imgList);
            productDTO.setProductImg(stringList);

            log.info("bidDate ? "+ tuple.get(qSales_bid.bidDate));
            LocalDate bidDate = tuple.get(qSales_bid.bidDate).toLocalDate();
            productDTO.setBidDate(bidDate);

            return productDTO;
        }).collect(Collectors.toList());

        return productDTOList;
    }

    @Override
    public Page<MyProductDTO> allPurchaseList_lt6(MySearchDTO mySearchDTO, Pageable pageable, Long mno) {
        QProduct qProduct = QProduct.product;
        QPurchase_bid qPurchase_bid = QPurchase_bid.purchase_bid;

        BooleanBuilder builder = new BooleanBuilder();
        if (mySearchDTO.getType().equals("ing")){
            builder.and(qPurchase_bid.isBuy.eq(false));
        }else if (mySearchDTO.getType().equals("finished")){
            builder.and(qPurchase_bid.isBuy.eq(true));
        }

        Predicate predicate = qPurchase_bid.mno.eq(mno)
                .and(qPurchase_bid.bidDate.between(
                        mySearchDTO.getSearchToDate().atStartOfDay(),
                        mySearchDTO.getSearchFromDate().atStartOfDay())
                ).and(builder);



        List<Tuple> purchaseList =
                jpaQueryFactory.select(qProduct.pno, qProduct.productName, qProduct.productImg, qPurchase_bid.bidDate)
                        .from(qProduct)
                        .join(qProduct.purchase_bids, qPurchase_bid)
                        .where(predicate)
                        .orderBy(qPurchase_bid.bidDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<MyProductDTO> productDTOList = purchaseList.stream().map(tuple -> {

            MyProductDTO productDTO = new MyProductDTO();
            productDTO.setPno(tuple.get(qProduct.pno));
            productDTO.setProductName(tuple.get(qProduct.productName));
            productDTO.setMno(mno);

            String[] imgArrays = tuple.get(qProduct.productImg).split(", ");
            List<String> productImgs = Arrays.asList(imgArrays);
            productDTO.setProductImg(productImgs);

            LocalDate bidDate = tuple.get(qPurchase_bid.bidDate).toLocalDate();
            productDTO.setBidDate(bidDate);

            return productDTO;
        }).collect(Collectors.toList());

        Long total =
                jpaQueryFactory.select(qProduct.count())
                        .from(qProduct)
                        .join(qProduct.purchase_bids, qPurchase_bid)
                        .where(predicate)
                        .fetchOne();


        return new PageImpl<>(productDTOList, pageable, total);
    }


}
