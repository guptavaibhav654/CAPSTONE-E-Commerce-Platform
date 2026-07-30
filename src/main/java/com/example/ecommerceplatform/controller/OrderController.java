package com.example.ecommerceplatform.controller;

import com.example.ecommerceplatform.dto.response.OrderResponse;
import com.example.ecommerceplatform.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponse> placeOrder(
            @PathVariable Long userId) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.placeOrder(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> orderHistory(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                orderService.getOrderHistory(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId));
    }

}