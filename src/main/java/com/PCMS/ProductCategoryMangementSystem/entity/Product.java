package com.PCMS.ProductCategoryMangementSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Product name Is Required")
    @Column(unique = true)
    private String productName;

    private String description;

    @NotNull(message = "Category Is Not Null")
    @ManyToOne
    @JoinColumn(name = "categoryId")
    @JsonBackReference
    private Category category;




}
