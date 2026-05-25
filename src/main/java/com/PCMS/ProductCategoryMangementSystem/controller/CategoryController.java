package com.PCMS.ProductCategoryMangementSystem.controller;

import com.PCMS.ProductCategoryMangementSystem.dto.CategoryDto;
import com.PCMS.ProductCategoryMangementSystem.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(dto));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> fetchCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.fetchCategoryById(categoryId));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> fetchAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(service.fetchAllCategories());
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
                                                      @PathVariable Long categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateCategory(categoryDto, categoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        service.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{categoryId}/product/{productId}")
    public ResponseEntity<CategoryDto> deleteProduct(@PathVariable Long categoryId,
                                                     @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteProduct(categoryId, productId));
    }
}
