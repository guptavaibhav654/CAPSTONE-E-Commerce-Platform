package com.example.ecommerceplatform.mapper;

import com.example.ecommerceplatform.dto.response.ProductResponse;
import com.example.ecommerceplatform.model.Product;

import java.math.BigDecimal;

public class ProductMapper {

    public static ProductResponse toResponse(Product product) {

        if (product == null) {
            return null;
        }

        return new ProductResponse(
                product.getProductId(),
                product.getProductName(),
                product.getCategory(),
                product.getPrice()
        );
    }
}