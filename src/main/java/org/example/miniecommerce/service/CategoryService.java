package org.example.miniecommerce.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.dto.category.CategoryResponse;
import org.example.miniecommerce.dto.category.CreateCategoryRequest;
import org.example.miniecommerce.entity.Category;
import org.example.miniecommerce.factory.CategoryFactory;
import org.example.miniecommerce.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Page<CategoryResponse> list(String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Category> categories = (keyword != null && !keyword.isBlank())
                ? categoryRepository.findByNameContainingIgnoreCase(keyword, pageable)
                : categoryRepository.findAll(pageable);

        return categories.map(CategoryFactory::toResponse);
    }

    public CategoryResponse create(CreateCategoryRequest req) {
        Category category = CategoryFactory.fromCreateRequest(req);
        category = categoryRepository.insert(category);
        return CategoryFactory.toResponse(category);
    }

    public CategoryResponse get(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));
        return CategoryFactory.toResponse(category);
    }

    public CategoryResponse update(Long id, CreateCategoryRequest req) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));

        CategoryFactory.updateCategory(category, req);
        category = categoryRepository.update(category);

        return CategoryFactory.toResponse(category);
    }

    public void delete(Long id) {
        categoryRepository.delete(id);
    }
}