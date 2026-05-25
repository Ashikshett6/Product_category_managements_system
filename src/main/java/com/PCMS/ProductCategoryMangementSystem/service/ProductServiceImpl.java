package com.PCMS.ProductCategoryMangementSystem.service;

import com.PCMS.ProductCategoryMangementSystem.dto.ProductDto;
import com.PCMS.ProductCategoryMangementSystem.entity.Category;
import com.PCMS.ProductCategoryMangementSystem.entity.Product;
import com.PCMS.ProductCategoryMangementSystem.exception.ResourceNotFoundException;
import com.PCMS.ProductCategoryMangementSystem.repository.CategoryRepo;
import com.PCMS.ProductCategoryMangementSystem.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ModelMapper modelMapper;
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    @Override
    public ProductDto createProduct(ProductDto dto, Long categoryId) {
        Product entity = modelMapper.map(dto, Product.class);

        Category category = categoryRepo
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " does not exist"));

        entity.setCategory(category);

        Product savedEntity = productRepo.save(entity);
        return modelMapper.map(savedEntity, ProductDto.class);
    }

    @Override
    public ProductDto updateNameDescription(ProductDto productDto, Long productId) {
        Product productEntity = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " does not exist"));

        // Manually update only non-null fields to avoid overwriting productId and category
        if (productDto.getProductName() != null && !productDto.getProductName().isBlank()) {
            productEntity.setProductName(productDto.getProductName());
        }
        if (productDto.getDescription() != null) {
            productEntity.setDescription(productDto.getDescription());
        }

        Product updatedProduct = productRepo.save(productEntity);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }
}
