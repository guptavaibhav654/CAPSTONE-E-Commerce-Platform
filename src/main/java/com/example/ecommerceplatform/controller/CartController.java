package com.example.ecommerceplatform.controller;

import com.example.ecommerceplatform.dto.request.AddCartRequest;
import com.example.ecommerceplatform.dto.request.UpdateCartRequest;
import com.example.ecommerceplatform.dto.response.CartResponse;
import com.example.ecommerceplatform.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
            @Valid @RequestBody AddCartRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartService.addToCart(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponse>> getCart(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.getCart(userId));
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartResponse> updateCart(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartRequest request) {

        return ResponseEntity.ok(
                cartService.updateCart(
                        cartItemId,
                        request));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long cartItemId) {

        cartService.removeFromCart(cartItemId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(
            @PathVariable Long userId) {

        cartService.clearCart(userId);

        return ResponseEntity.noContent().build();
    }

}