package com.PCMS.ProductCategoryMangementSystem.repository;

import com.PCMS.ProductCategoryMangementSystem.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {

}
