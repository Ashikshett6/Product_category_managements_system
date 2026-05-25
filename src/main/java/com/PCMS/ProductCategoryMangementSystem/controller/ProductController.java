package com.PCMS.ProductCategoryMangementSystem.controller;

import com.PCMS.ProductCategoryMangementSystem.dto.ProductDto;
import com.PCMS.ProductCategoryMangementSystem.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping("/{categoryId}")
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto dto,
                                                    @PathVariable Long categoryId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(dto, categoryId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto,
                                                    @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateNameDescription(productDto, productId));
    }
}
