package com.example.cream_jpa.kream.repository.queryDSL;

import com.example.cream_jpa.kream.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryRepository{

    int getMinPrice(Long pno); // 최저판매가
}
