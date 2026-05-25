package com.PCMS.ProductCategoryMangementSystem.service;

import com.PCMS.ProductCategoryMangementSystem.dto.CategoryDto;
import com.PCMS.ProductCategoryMangementSystem.entity.Category;
import com.PCMS.ProductCategoryMangementSystem.entity.Product;
import com.PCMS.ProductCategoryMangementSystem.exception.ResourceNotFoundException;
import com.PCMS.ProductCategoryMangementSystem.repository.CategoryRepo;
import com.PCMS.ProductCategoryMangementSystem.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepo repo;
    private final ModelMapper modelMapper;
    private final ProductRepo productRepo;

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        if (dto == null) {
            throw new RuntimeException("Category cannot be null");
        }
        //converting dto to entity type using modelmapper
        Category entity = modelMapper.map(dto, Category.class);
        //saving the converted entity into db
        Category savedCategory = repo.save(entity);

        //converting saved entity back to dto
        CategoryDto categoryDto = modelMapper.map(savedCategory, CategoryDto.class);

        return categoryDto;
    }

    @Override
    public CategoryDto fetchCategoryById(Long categoryId) {
        Category entity = repo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " does not exist"));

        CategoryDto dto = modelMapper.map(entity, CategoryDto.class);
        return dto;
    }

    @Override
    public List<CategoryDto> fetchAllCategories() {
        List<Category> categories = repo.findAll();

        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category categoryEntity = repo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " does not exist"));

        // Manually update only non-null fields to avoid overwriting categoryId and products
        if (categoryDto.getCategoryName() != null && !categoryDto.getCategoryName().isBlank()) {
            categoryEntity.setCategoryName(categoryDto.getCategoryName());
        }
        if (categoryDto.getDescription() != null) {
            categoryEntity.setDescription(categoryDto.getDescription());
        }

        Category updatedCategory = repo.save(categoryEntity);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = repo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " does not exist"));
        repo.delete(category);  // Cascade will delete all products
    }

    @Override
    public CategoryDto deleteProduct(Long categoryId, Long productId) {
        Category categoryEntity = repo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " does not exist"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " does not exist"));

        // Verify that the product belongs to this category
        if (product.getCategory() == null || product.getCategory().getCategoryId() != categoryId.longValue()) {
            throw new ResourceNotFoundException("Product with id " + productId + " does not belong to category with id " + categoryId);
        }


        productRepo.delete(product);
        // Refresh the category to get the updated state without the deleted product
        Category updatedCategory = repo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " does not exist"));
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }
}
