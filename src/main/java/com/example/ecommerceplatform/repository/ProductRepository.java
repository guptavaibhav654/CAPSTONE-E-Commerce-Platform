package com.example.ecommerceplatform.repository;

import com.example.ecommerceplatform.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategory(
            String category,
            Pageable pageable);

    List<Product> findByPriceBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice);

    Page<Product> findByProductNameContainingIgnoreCase(
            String keyword,
            Pageable pageable);

    List<Product> findTop5ByOrderByPriceDesc();
}