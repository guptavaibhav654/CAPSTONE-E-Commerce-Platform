package com.example.ecommerceplatform.mapper;

import com.example.ecommerceplatform.dto.response.CartResponse;
import com.example.ecommerceplatform.model.CartItem;

import java.math.BigDecimal;

public final class CartMapper {

    private CartMapper() {
    }

    public static CartResponse toResponse(CartItem cartItem) {

        if (cartItem == null) {
            return null;
        }

        BigDecimal price = cartItem.getProduct().getPrice();

        BigDecimal totalPrice =
                price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return new CartResponse(
                cartItem.getCartItemId(),
                cartItem.getProduct().getProductId(),
                cartItem.getProduct().getProductName(),
                cartItem.getQuantity(),
                price,
                totalPrice
        );
    }
}