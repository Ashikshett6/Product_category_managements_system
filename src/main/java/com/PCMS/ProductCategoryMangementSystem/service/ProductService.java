package com.PCMS.ProductCategoryMangementSystem.service;

import com.PCMS.ProductCategoryMangementSystem.dto.ProductDto;
import jakarta.validation.Valid;

public interface ProductService {
    ProductDto createProduct(@Valid ProductDto dto, Long categoryId);

    ProductDto updateNameDescription(ProductDto productDto, Long productId);
}
