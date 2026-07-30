package com.example.ecommerceplatform.repository;

import com.example.ecommerceplatform.model.Order;
import com.example.ecommerceplatform.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByOrderDateDesc(User user);

    Page<Order> findByUser(
            User user,
            Pageable pageable);

    long countByUser(User user);

    List<Order> findByUserAndOrderDateBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end);
}