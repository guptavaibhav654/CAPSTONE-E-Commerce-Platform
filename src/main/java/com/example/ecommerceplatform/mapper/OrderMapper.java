package com.example.ecommerceplatform.mapper;

import com.example.ecommerceplatform.dto.response.OrderItemResponse;
import com.example.ecommerceplatform.dto.response.OrderResponse;
import com.example.ecommerceplatform.model.Order;
import com.example.ecommerceplatform.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {

        if (order == null) {
            return null;
        }

        List<OrderItemResponse> items = order.getOrderItems()
                .stream()
                .map(OrderMapper::toItemResponse)
                .toList();

        return new OrderResponse(
                order.getOrderId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                items.size(),
                items
        );
    }

    private static OrderItemResponse toItemResponse(OrderItem item) {

        BigDecimal unitPrice = item.getPrice();

        BigDecimal totalPrice =
                unitPrice.multiply(
                        BigDecimal.valueOf(item.getQuantity()));

        return new OrderItemResponse(
                item.getProduct().getProductId(),
                item.getProduct().getProductName(),
                item.getQuantity(),
                unitPrice,
                totalPrice
        );
    }
}