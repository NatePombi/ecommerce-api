package com.nathan.ecommerceapi.category.repository;

import com.nathan.ecommerceapi.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {


}
