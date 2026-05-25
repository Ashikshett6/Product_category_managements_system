package com.PCMS.ProductCategoryMangementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long productId;

    @NotBlank(message = "Product name Is Required")
    private String productName;

    private String description;
}

