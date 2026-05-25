package com.PCMS.ProductCategoryMangementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private long categoryId;

    @NotBlank(message = "category name Is required.")
    private String categoryName;
    private String description;
    private List<ProductDto> products;
}
