package com.example.ecommerceplatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private final Long orderId;

    private final LocalDateTime orderDate;

    private final BigDecimal totalAmount;

    private final Integer totalItems;

    private final List<OrderItemResponse> items;
}