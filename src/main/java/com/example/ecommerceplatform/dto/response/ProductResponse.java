package com.example.ecommerceplatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductResponse {

    private final Long productId;

    private final String productName;

    private final String category;

    private final BigDecimal price;
}