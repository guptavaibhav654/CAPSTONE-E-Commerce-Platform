package com.example.ecommerceplatform.service.impl;

import com.example.ecommerceplatform.dto.request.AddCartRequest;
import com.example.ecommerceplatform.dto.request.UpdateCartRequest;
import com.example.ecommerceplatform.dto.response.CartResponse;
import com.example.ecommerceplatform.exception.BadRequestException;
import com.example.ecommerceplatform.exception.ResourceNotFoundException;
import com.example.ecommerceplatform.mapper.CartMapper;
import com.example.ecommerceplatform.model.CartItem;
import com.example.ecommerceplatform.model.Product;
import com.example.ecommerceplatform.model.User;
import com.example.ecommerceplatform.repository.CartRepository;
import com.example.ecommerceplatform.repository.ProductRepository;
import com.example.ecommerceplatform.repository.UserRepository;
import com.example.ecommerceplatform.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public CartResponse addToCart(AddCartRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found."));

        CartItem existingItem = cartRepository
                .findByUserAndProduct(user, product)
                .orElse(null);

        if (existingItem != null) {

            existingItem.setQuantity(
                    existingItem.getQuantity()
                            + request.getQuantity());

            return CartMapper.toResponse(
                    cartRepository.save(existingItem));
        }

        CartItem cartItem = new CartItem();

        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(request.getQuantity());

        return CartMapper.toResponse(
                cartRepository.save(cartItem));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartResponse> getCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."));

        return cartRepository.findByUser(user)
                .stream()
                .map(CartMapper::toResponse)
                .toList();
    }

    @Override
    public CartResponse updateCart(Long cartItemId,
                                   UpdateCartRequest request) {

        CartItem cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found."));

        if (request.getQuantity() <= 0) {
            throw new BadRequestException(
                    "Quantity must be greater than zero.");
        }

        cartItem.setQuantity(request.getQuantity());

        return CartMapper.toResponse(
                cartRepository.save(cartItem));
    }

    @Override
    public void removeFromCart(Long cartItemId) {

        CartItem cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found."));

        cartRepository.delete(cartItem);
    }

    @Override
    public void clearCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."));

        cartRepository.deleteByUser(user);
    }

}