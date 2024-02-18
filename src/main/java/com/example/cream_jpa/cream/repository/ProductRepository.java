package com.example.cream_jpa.cream.repository;

import com.example.cream_jpa.cream.entity.Product;
import com.example.cream_jpa.cream.repository.queryDSL.QueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QueryRepository {

    Page<Product> findAllByIsDelFalse(Pageable pageable);
}
