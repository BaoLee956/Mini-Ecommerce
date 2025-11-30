package org.example.miniecommerce.service;

import org.example.miniecommerce.dto.product.CreateProductRequest;
import org.example.miniecommerce.dto.product.ProductResponse;
import org.example.miniecommerce.dto.product.UpdateProductRequest;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<ProductResponse> list(String keyword, Long categoryId, int page, int size);
    ProductResponse create(CreateProductRequest req);
    ProductResponse get(Long id);
    ProductResponse update(Long id, UpdateProductRequest req);
    void delete(Long id);
}
