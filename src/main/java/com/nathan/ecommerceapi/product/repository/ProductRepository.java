package com.nathan.ecommerceapi.product.repository;

import com.nathan.ecommerceapi.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByNameIgnoreCase(String name);
}
