package com.example.ecommerceplatform.repository;

import com.example.ecommerceplatform.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
