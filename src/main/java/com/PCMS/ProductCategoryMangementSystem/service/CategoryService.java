package com.PCMS.ProductCategoryMangementSystem.service;

import com.PCMS.ProductCategoryMangementSystem.dto.CategoryDto;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(@Valid CategoryDto dto);

    CategoryDto fetchCategoryById(Long categoryId);

    List<CategoryDto> fetchAllCategories();

    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);

    void deleteCategory(Long categoryId);

    CategoryDto deleteProduct(Long categoryId, Long productId);
}
