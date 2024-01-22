package com.example.cream_jpa.kream.repository;

import com.example.cream_jpa.kream.entity.Product;
import com.example.cream_jpa.kream.repository.queryDSL.QueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QueryRepository {

    Page<Product> findAllByIsDelFalse(Pageable pageable);
}
