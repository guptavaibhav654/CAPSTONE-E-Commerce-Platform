package com.example.ecommerceplatform.service;

import com.example.ecommerceplatform.dto.request.AddCartRequest;
import com.example.ecommerceplatform.dto.request.UpdateCartRequest;
import com.example.ecommerceplatform.dto.response.CartResponse;

import java.util.List;

public interface CartService {

    CartResponse addToCart(AddCartRequest request);

    List<CartResponse> getCart(Long userId);

    CartResponse updateCart(
            Long cartItemId,
            UpdateCartRequest request);

    void removeFromCart(Long cartItemId);

    void clearCart(Long userId);

}