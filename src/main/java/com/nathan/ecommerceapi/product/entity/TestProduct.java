package com.nathan.ecommerceapi.product.entity;

import com.nathan.ecommerceapi.category.entity.Category;

import java.math.BigDecimal;

public class TestProduct extends Product{
    public TestProduct(Long id, String name, Category category, BigDecimal price, String description, String imageUrl, Integer stock) {
        super(id, name, category, price, description, imageUrl, stock);
    }
}
