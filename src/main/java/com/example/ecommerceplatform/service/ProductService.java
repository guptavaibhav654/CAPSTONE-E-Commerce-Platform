package com.example.ecommerceplatform.service;

import com.example.ecommerceplatform.dto.request.ProductRequest;
import com.example.ecommerceplatform.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductResponse addProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    Page<ProductResponse> getAllProducts(
            int page,
            int size,
            String sortBy,
            String direction);

    ProductResponse updateProduct(
            Long id,
            ProductRequest request);

    void deleteProduct(Long id);

    Page<ProductResponse> getProductsByCategory(
            String category,
            int page,
            int size);

    Page<ProductResponse> searchProducts(
            String keyword,
            int page,
            int size);
}
