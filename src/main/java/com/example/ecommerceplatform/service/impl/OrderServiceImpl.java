package com.example.ecommerceplatform.service.impl;

import com.example.ecommerceplatform.dto.response.OrderResponse;
import com.example.ecommerceplatform.exception.BadRequestException;
import com.example.ecommerceplatform.exception.ResourceNotFoundException;
import com.example.ecommerceplatform.mapper.OrderMapper;
import com.example.ecommerceplatform.model.CartItem;
import com.example.ecommerceplatform.model.Order;
import com.example.ecommerceplatform.model.OrderItem;
import com.example.ecommerceplatform.model.User;
import com.example.ecommerceplatform.repository.CartRepository;
import com.example.ecommerceplatform.repository.OrderRepository;
import com.example.ecommerceplatform.repository.UserRepository;
import com.example.ecommerceplatform.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Override
    public OrderResponse placeOrder(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."));

        List<CartItem> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new BadRequestException(
                    "Cart is empty.");
        }

        Order order = new Order();

        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());

            orderItem.setPrice(
                    cartItem.getProduct().getPrice());

            BigDecimal itemPrice = cartItem.getProduct().getPrice();
            totalAmount = totalAmount.add(
                    itemPrice.multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())));

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cartRepository.deleteAll(cartItems);

        return OrderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrderHistory(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."));

        return orderRepository.findByUserOrderByOrderDateDesc(user)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found."));

        return OrderMapper.toResponse(order);
    }

}