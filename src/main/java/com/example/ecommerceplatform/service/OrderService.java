package com.example.ecommerceplatform.service;

import com.example.ecommerceplatform.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(Long userId);

    List<OrderResponse> getOrderHistory(Long userId);

    OrderResponse getOrderById(Long orderId);
}
