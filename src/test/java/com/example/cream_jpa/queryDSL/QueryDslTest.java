package com.example.cream_jpa.queryDSL;

import com.example.cream_jpa.config.QueryDSLConfig;
import com.example.cream_jpa.cream.entity.Product;
import com.example.cream_jpa.cream.entity.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QueryDslTest {

    @PersistenceContext
    EntityManager entityManager;

    JPAQueryFactory jpaQueryFactory;


    @Test
    void select() {
        // QueryDSL 코드를 개선하여 QProduct 클래스 사용
        QProduct qProduct = QProduct.product;
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // 쿼리 실행 및 결과 확인
        Product result = jpaQueryFactory
                .selectFrom(qProduct)
                .where(qProduct.productName.eq("된장포스"))
                .fetchFirst();

        // 검증
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("된장포스");
    }
}
