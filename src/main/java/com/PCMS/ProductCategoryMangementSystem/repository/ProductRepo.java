package com.PCMS.ProductCategoryMangementSystem.repository;

import com.PCMS.ProductCategoryMangementSystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Long> {
}
