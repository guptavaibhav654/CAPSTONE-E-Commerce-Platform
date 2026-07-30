package com.example.ecommerceplatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CartResponse {

    private final Long cartItemId;

    private final Long productId;

    private final String productName;

    private final Integer quantity;

    private final BigDecimal price;

    private final BigDecimal totalPrice;
}