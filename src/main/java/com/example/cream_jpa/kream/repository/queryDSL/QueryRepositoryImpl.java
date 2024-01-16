package com.example.cream_jpa.kream.repository.queryDSL;
import com.example.cream_jpa.kream.entity.QProduct;
import com.example.cream_jpa.kream.entity.QPurchase_bid;
import com.example.cream_jpa.kream.entity.QSales_bid;
import com.querydsl.core.types.Template;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.expression.common.ExpressionUtils;

import java.time.LocalDate;

import static com.querydsl.core.types.dsl.Expressions.dateTemplate;
import static java.util.Optional.ofNullable;

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


}
