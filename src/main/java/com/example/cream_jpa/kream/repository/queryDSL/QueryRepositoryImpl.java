package com.example.cream_jpa.kream.repository.queryDSL;
import com.example.cream_jpa.kream.entity.QProduct;
import com.example.cream_jpa.kream.entity.QPurchase_bid;
import com.example.cream_jpa.kream.entity.QSales_bid;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class QueryRepositoryImpl extends QuerydslRepositorySupport implements QueryRepository {


    private final JPAQueryFactory jpaQueryFactory;

    public QueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(QProduct.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public int getMinSalesPrice(Long pno) {

        QProduct qProduct = QProduct.product;
        QSales_bid qSales_bid = QSales_bid.sales_bid;

        int minPrice =
                jpaQueryFactory.select(qSales_bid.salesPrice.min())
                        .from(qProduct)
                        .join(qProduct.sales_bids, qSales_bid)
                        .where(qProduct.pno.eq(pno))
                        .fetchOne();

        return minPrice;
    }

    @Override
    public int getMaxPurchasePrice(Long pno) {

        QProduct qProduct = QProduct.product;
        QPurchase_bid qPurchase_bid = QPurchase_bid.purchase_bid;

        Integer maxPrice =
                jpaQueryFactory.select(qPurchase_bid.purchasePrice.max())
                        .from(qProduct)
                        .join(qProduct.purchase_bids, qPurchase_bid)
                        .where(qProduct.pno.eq(pno))
                        .fetchOne();
        if (maxPrice != null){
            return maxPrice;
        }else{
            return 0;
        }


    }


}
