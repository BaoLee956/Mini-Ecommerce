package org.example.miniecommerce.service;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.dto.product.CreateProductRequest;
import org.example.miniecommerce.dto.product.ProductResponse;
import org.example.miniecommerce.dto.product.UpdateProductRequest;
import org.example.miniecommerce.entity.Category;
import org.example.miniecommerce.entity.Product;
import org.example.miniecommerce.factory.ProductFactory;
import org.example.miniecommerce.repository.CategoryRepository;
import org.example.miniecommerce.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<ProductResponse> list(String keyword, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products;

        if (keyword != null && !keyword.isBlank()) {
            products = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(ProductFactory::toResponse);
    }

    @Override
    public ProductResponse create(CreateProductRequest req) {
        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
        Product product = ProductFactory.fromCreateRequest(req, category);
        return ProductFactory.toResponse(productRepository.insert(product));
    }

    @Override
    public ProductResponse get(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        return ProductFactory.toResponse(product);
    }

    @Override
    public ProductResponse update(Long id, UpdateProductRequest req) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
        ProductFactory.updateProduct(product, req, category);
        return ProductFactory.toResponse(productRepository.update(product));
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        productRepository.delete(product.getId());
    }
}
